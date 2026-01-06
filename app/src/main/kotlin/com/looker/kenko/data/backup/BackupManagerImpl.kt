/*
 * Copyright (C) 2025 LooKeR & Contributors
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.looker.kenko.data.backup

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.looker.kenko.data.local.KenkoDatabase
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.model.settings.BackupInterval
import com.looker.kenko.di.IoDispatcher
import com.looker.kenko.utils.DateFormat
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

class BackupManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val database: KenkoDatabase,
) : BackupManager {

    private val databaseName = "kenko_database"
    private val datastoreFileName = "settings.preferences_pb"

    override suspend fun createBackup(destinationUri: Uri): BackupResult =
        withContext(dispatcher) {
            try {
                // Checkpoint database to ensure WAL is flushed
                database.query("PRAGMA wal_checkpoint(TRUNCATE)", null).close()

                val tempZipFile = File(context.cacheDir, "temp_backup.zip")

                ZipOutputStream(tempZipFile.outputStream()).use { zipOut ->
                    val dbFiles = databaseFiles()
                    if (dbFiles.isNotEmpty()) dbFiles.forEach { zipOut.addEntry(it.name, it) }

                    val datastoreFile = datastoreFile()
                    if (datastoreFile.exists()) zipOut.addEntry(datastoreFile.name, datastoreFile)
                }

                tempZipFile.copyTo(destinationUri)
                tempZipFile.delete()

                BackupResult.Success
            } catch (e: Exception) {
                e.printStackTrace()
                BackupResult.Error("Failed to create backup: ${e.message}", e)
            }
        }

    override suspend fun restoreBackup(sourceUri: Uri): BackupResult =
        withContext(dispatcher) {
            try {
                val tempDir = File(context.cacheDir, "temp_restore")
                tempDir.mkdirs()

                extractZipFromUri(sourceUri, tempDir)

                val dbFile = File(tempDir, databaseName)
                if (!dbFile.exists()) {
                    tempDir.deleteRecursively()
                    return@withContext BackupResult.Error("Invalid backup: database file not found")
                }

                database.close()

                replaceDatabaseFiles(tempDir)
                replaceDatastoreFile(tempDir)
                tempDir.deleteRecursively()

                BackupResult.Success
            } catch (e: Exception) {
                BackupResult.Error("Failed to restore backup: ${e.message}", e)
            }
        }

    override fun schedulePeriodicBackup(interval: BackupInterval, destinationUri: Uri) {
        if (interval == BackupInterval.Off) {
            cancelScheduledBackup()
            return
        }

        val workRequest = PeriodicWorkRequestBuilder<BackupWorker>(
            interval.hours,
            TimeUnit.HOURS,
        ).setConstraints(
            Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build(),
        ).setInputData(
            workDataOf(BackupWorker.KEY_BACKUP_URI to destinationUri.toString()),
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            BACKUP_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest,
        )
    }

    override fun cancelScheduledBackup() {
        WorkManager.getInstance(context).cancelUniqueWork(BACKUP_WORK_NAME)
    }

    private fun databaseFiles(): List<File> {
        val dbPath = context.getDatabasePath(databaseName)
        return listOf(
            dbPath,
            File(dbPath.path + "-shm"),
            File(dbPath.path + "-wal"),
        ).filter { it.exists() }
    }

    private fun datastoreFile(): File {
        val datastoreDir = File(context.filesDir, "datastore")
        return File(datastoreDir, datastoreFileName)
    }

    private fun ZipOutputStream.addEntry(name: String, file: File) {
        putNextEntry(ZipEntry(name))
        file.inputStream().use { it.copyTo(this) }
        closeEntry()
    }

    private suspend fun File.copyTo(destinationUri: Uri) = withContext(dispatcher) {
        if (destinationUri.scheme == "file") {
            val destFile = File(destinationUri.path!!)
            copyTo(destFile, overwrite = true)
            return@withContext
        }

        val treeUri = destinationUri.toTreeUri()

        val treeDoc = DocumentFile.fromTreeUri(context, treeUri)
            ?: error("Cannot access directory: $treeUri")

        val fileName = backupFileName(localDate)
        val backupFile = treeDoc.findFile(fileName)
            ?: treeDoc.createFile("application/zip", fileName)
            ?: error("Cannot create backup file in: $treeUri")

        context.contentResolver.openOutputStream(backupFile.uri)?.use { output ->
            inputStream().use { input ->
                input.copyTo(output)
            }
        } ?: error("Cannot open output stream for URI: ${backupFile.uri}")
    }

    /**
     * Extracts the tree URI from a potentially malformed URI that may have a filename appended.
     * Tree URIs have the pattern: content://authority/tree/treeId
     * If a filename was appended (e.g., /kenko_backup.zip), strip it.
     */
    private fun Uri.toTreeUri(): Uri {
        val uriString = toString()
        val treeIndex = uriString.indexOf("/tree/")
        if (treeIndex == -1) return this

        val treeStart = treeIndex + "/tree/".length
        val nextSlash = uriString.indexOf('/', treeStart)

        return if (nextSlash != -1) {
            Uri.parse(uriString.take(nextSlash))
        } else {
            this
        }
    }

    private fun extractZipFromUri(sourceUri: Uri, destDir: File) {
        val inputStream = when (sourceUri.scheme) {
            "file" -> File(sourceUri.path!!).inputStream()
            else -> context.contentResolver.openInputStream(sourceUri)
                ?: error("Cannot open input stream for URI: $sourceUri")
        }

        ZipInputStream(inputStream).use { zipIn ->
            var entry = zipIn.nextEntry
            while (entry != null) {
                val file = File(destDir, entry.name)
                if (!entry.isDirectory) {
                    file.parentFile?.mkdirs()
                    file.outputStream().use { output ->
                        zipIn.copyTo(output)
                    }
                }
                zipIn.closeEntry()
                entry = zipIn.nextEntry
            }
        }
    }

    private fun replaceDatabaseFiles(sourceDir: File) {
        val dbPath = context.getDatabasePath(databaseName)
        val dbFiles = listOf(
            databaseName,
            "$databaseName-shm",
            "$databaseName-wal",
        )

        dbFiles.forEach { fileName ->
            val sourceFile = File(sourceDir, fileName)
            val destFile = if (fileName == databaseName) {
                dbPath
            } else {
                File(dbPath.path.replace(databaseName, fileName))
            }

            if (sourceFile.exists()) {
                destFile.parentFile?.mkdirs()
                sourceFile.copyTo(destFile, overwrite = true)
            } else {
                destFile.delete()
            }
        }
    }

    private fun replaceDatastoreFile(sourceDir: File) {
        val sourceFile = File(sourceDir, datastoreFileName)
        val datastoreDir = File(context.filesDir, "datastore")
        val destFile = File(datastoreDir, datastoreFileName)

        if (sourceFile.exists()) {
            datastoreDir.mkdirs()
            sourceFile.copyTo(destFile, overwrite = true)
        }
    }

    fun backupFileName(date: LocalDate): String = buildString {
        append("kenko_backup_")
        append(DateFormat.BackupName.format(date))
    }

    companion object {
        const val BACKUP_WORK_NAME = "kenko_backup_work"
    }
}
