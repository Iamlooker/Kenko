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

package com.looker.kenko

import android.content.Context
import androidx.core.net.toUri
import androidx.test.core.app.ApplicationProvider
import com.looker.kenko.data.backup.BackupManager
import com.looker.kenko.data.backup.BackupResult
import com.looker.kenko.data.local.model.SetType
import com.looker.kenko.data.model.PlanItem
import com.looker.kenko.data.model.RepsInReserve
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.data.repository.SessionRepo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import java.io.File
import javax.inject.Inject
import kotlin.random.Random
import kotlin.test.Ignore
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DayOfWeek
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class BackupManagerTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var backupManager: BackupManager

    @Inject
    lateinit var sessionRepo: SessionRepo

    @Inject
    lateinit var planRepo: PlanRepo

    @Inject
    lateinit var exerciseRepo: ExerciseRepo

    private lateinit var context: Context
    private lateinit var backupDir: File

    @Before
    fun setup() {
        hiltRule.inject()
        context = ApplicationProvider.getApplicationContext()
        backupDir = File(context.cacheDir, "test_backups").apply {
            mkdirs()
        }
    }

    @After
    fun tearDown() {
        backupDir.deleteRecursively()
    }

    @Test
    fun createBackup_createsValidZipFile() = runTest {
        val backupFile = File(backupDir, "test_backup.zip")
        val result = backupManager.createBackup(backupFile.toUri())

        assertIs<BackupResult.Success>(result)
        assertTrue(backupFile.exists())
        assertTrue(backupFile.length() > 0)
    }

    @Test
    fun createBackup_containsDatabaseFile() = runTest {
        val backupFile = File(backupDir, "test_backup_db.zip")
        val result = backupManager.createBackup(backupFile.toUri())

        assertIs<BackupResult.Success>(result)

        // Verify zip contains database
        val extractDir = File(backupDir, "extracted")
        extractDir.mkdirs()

        java.util.zip.ZipFile(backupFile).use { zip ->
            val entries = zip.entries().toList().map { it.name }
            assertTrue(entries.any { it.contains("kenko_database") })
        }
    }

    @Test
    @Ignore("Don't know how to restart app so we can check if data was preserved")
    fun backupAndRestore_preservesData() = runTest {
        // Create some test data
        val planId = planRepo.createPlan("backup_test_plan")
        val exercises = (1..3).mapNotNull { exerciseRepo.get(it) }
        exercises.forEach {
            planRepo.addItem(
                PlanItem(
                    dayOfWeek = DayOfWeek(Random.nextInt(1, 5)),
                    exercise = it,
                    planId = planId,
                ),
            )
        }
        planRepo.setCurrent(planId)

        val sessionId = sessionRepo.getSessionIdOrCreate(localDate)
        val sets = (1..5).map {
            Set(
                repsOrDuration = 12,
                weight = 50F,
                type = SetType.Standard,
                exercise = exercises.first(),
                rir = RepsInReserve(2),
            )
        }
        sets.forEach { sessionRepo.addSet(sessionId, it) }

        // Get counts before backup
        val exerciseCountBefore = exerciseRepo.stream.first().size
        val planItemsBefore = planRepo.getPlanItems(planId).size
        val setCountBefore = sessionRepo.getSets(sessionId).size

        // Create backup
        val backupFile = File(backupDir, "data_backup.zip")
        val backupResult = backupManager.createBackup(backupFile.toUri())
        assertIs<BackupResult.Success>(backupResult)

        // Restore backup
        val restoreResult = backupManager.restoreBackup(backupFile.toUri())
        assertIs<BackupResult.Success>(restoreResult)

        // Verify data is preserved
        val exerciseCountAfter = exerciseRepo.stream.first().size
        assertEquals(exerciseCountBefore, exerciseCountAfter)
    }

    @Test
    fun restoreBackup_withInvalidFile_returnsError() = runTest {
        val invalidFile = File(backupDir, "invalid.zip")
        invalidFile.writeText("not a zip file")

        val result = backupManager.restoreBackup(invalidFile.toUri())

        assertIs<BackupResult.Error>(result)
    }

    @Test
    fun restoreBackup_withNonExistentFile_returnsError() = runTest {
        val nonExistentFile = File(backupDir, "does_not_exist.zip")

        val result = backupManager.restoreBackup(nonExistentFile.toUri())

        assertIs<BackupResult.Error>(result)
    }

    @Test
    fun createBackup_withInvalidUri_returnsError() = runTest {
        val invalidUri = "content://invalid/path".toUri()

        val result = backupManager.createBackup(invalidUri)

        assertIs<BackupResult.Error>(result)
    }
}
