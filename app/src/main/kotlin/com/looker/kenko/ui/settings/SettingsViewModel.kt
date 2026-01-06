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

package com.looker.kenko.ui.settings

import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.data.backup.BackupManager
import com.looker.kenko.data.backup.BackupResult
import com.looker.kenko.data.model.settings.BackupInterval
import com.looker.kenko.data.model.settings.ColorPalettes
import com.looker.kenko.data.model.settings.Theme
import com.looker.kenko.data.repository.SettingsRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: SettingsRepo,
    private val backupManager: BackupManager,
) : ViewModel() {

    private val _backupState = MutableStateFlow(BackupState())

    val state: StateFlow<SettingsUiData> = combine(
        repo.stream,
        _backupState,
    ) { settings, backupState ->
        SettingsUiData(
            selectedTheme = settings.theme,
            selectedColorPalette = settings.colorPalette,
            backupUri = settings.backupUri,
            backupInterval = settings.backupInterval,
            lastBackupTime = settings.lastBackupTime,
            isBackingUp = backupState.isBackingUp,
            isRestoring = backupState.isRestoring,
            backupMessage = backupState.message,
        )
    }.asStateFlow(
        SettingsUiData(
            selectedTheme = Theme.System,
            selectedColorPalette = ColorPalettes.Default,
            backupUri = null,
            backupInterval = BackupInterval.Off,
            lastBackupTime = null,
            isBackingUp = false,
            isRestoring = false,
            backupMessage = null,
        ),
    )

    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            repo.setTheme(theme)
        }
    }

    fun updateColorPalette(colorPalette: ColorPalettes) {
        viewModelScope.launch {
            repo.setColorPalette(colorPalette)
        }
    }

    fun setBackupLocation(uri: Uri) {
        viewModelScope.launch {
            repo.setBackupUri(uri.toString())
            backupManager.schedulePeriodicBackup(state.value.backupInterval, uri)
        }
    }

    fun setBackupInterval(interval: BackupInterval) {
        viewModelScope.launch {
            repo.setBackupInterval(interval)
            val backupUri = state.value.backupUri?.toUri()
            if (backupUri != null) {
                backupManager.schedulePeriodicBackup(interval, backupUri)
            }
        }
    }

    fun backupNow() {
        val backupUri = state.value.backupUri?.toUri() ?: return

        viewModelScope.launch {
            _backupState.update { it.copy(isBackingUp = true, message = null) }

            when (backupManager.createBackup(backupUri)) {
                is BackupResult.Success -> {
                    repo.setLastBackupTime(Clock.System.now())
                    _backupState.update {
                        it.copy(isBackingUp = false, message = BackupMessage.BackupSuccess)
                    }
                }

                is BackupResult.Error -> {
                    _backupState.update {
                        it.copy(isBackingUp = false, message = BackupMessage.BackupFailed)
                    }
                }
            }
        }
    }

    fun restore(uri: Uri) {
        viewModelScope.launch {
            _backupState.update { it.copy(isRestoring = true, message = null) }

            when (backupManager.restoreBackup(uri)) {
                is BackupResult.Success -> {
                    repo.setBackupUri(null)
                    repo.setLastBackupTime(null)
                    _backupState.update {
                        it.copy(isRestoring = false, message = BackupMessage.RestoreSuccess)
                    }
                }

                is BackupResult.Error -> {
                    _backupState.update {
                        it.copy(isRestoring = false, message = BackupMessage.RestoreFailed)
                    }
                }
            }
        }
    }

    fun clearBackupMessage() {
        _backupState.update { it.copy(message = null) }
    }
}

private data class BackupState(
    val isBackingUp: Boolean = false,
    val isRestoring: Boolean = false,
    val message: BackupMessage? = null,
)

enum class BackupMessage {
    BackupSuccess,
    BackupFailed,
    RestoreSuccess,
    RestoreFailed,
}

@Stable
data class SettingsUiData(
    val selectedTheme: Theme,
    val selectedColorPalette: ColorPalettes,
    val backupUri: String?,
    val backupInterval: BackupInterval,
    val lastBackupTime: Instant?,
    val isBackingUp: Boolean,
    val isRestoring: Boolean,
    val backupMessage: BackupMessage?,
)
