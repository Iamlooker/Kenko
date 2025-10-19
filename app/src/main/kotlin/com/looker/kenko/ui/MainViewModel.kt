/*
 * Copyright (C) 2025. LooKeR & Contributors
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

package com.looker.kenko.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.data.model.settings.Theme
import com.looker.kenko.data.repository.PerformanceRepo
import com.looker.kenko.data.repository.SettingsRepo
import com.looker.kenko.ui.theme.colorSchemes.ColorSchemes
import com.looker.kenko.ui.theme.colorSchemes.zestfulColorSchemes
import com.looker.kenko.ui.theme.dynamicColorSchemes
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel
class MainViewModel @Inject constructor(
    repo: SettingsRepo,
    performanceRepo: PerformanceRepo,
    context: Context,
) : ViewModel() {

    val theme: StateFlow<Theme> = repo.get { theme }
        .asStateFlow(Theme.System)

    val colorScheme: StateFlow<ColorSchemes> = repo.stream
        .map { it.colorPalette.scheme ?: dynamicColorSchemes(context) ?: zestfulColorSchemes }
        .asStateFlow(zestfulColorSchemes)

    val isOnboardingDone: Boolean = runBlocking { repo.stream.first().isOnboardingDone }

    init {
        viewModelScope.launch {
            performanceRepo.updateModifiers()
        }
    }
}
