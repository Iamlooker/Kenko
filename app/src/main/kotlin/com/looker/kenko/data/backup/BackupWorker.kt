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
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.looker.kenko.data.repository.SettingsRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlin.time.Clock

@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val backupManager: BackupManager,
    private val settingsRepo: SettingsRepo,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val backupUri = inputData.getString(KEY_BACKUP_URI)?.toUri() ?: return Result.failure()

        return when (val result = backupManager.createBackup(backupUri)) {
            is BackupResult.Success -> {
                settingsRepo.setLastBackupTime(Clock.System.now())
                Result.success(workDataOf(KEY_BACKED_UP_URI to backupUri.toString()))
            }

            is BackupResult.Error -> Result.retry()
        }
    }

    companion object {
        const val KEY_BACKUP_URI = "backup_uri"
        const val KEY_BACKED_UP_URI = "backup_uri"
    }
}
