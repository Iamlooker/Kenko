package com.looker.kenko.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.looker.kenko.data.model.settings.Theme
import com.looker.kenko.data.repository.SettingsRepo
import com.looker.kenko.ui.theme.colorSchemes.ColorSchemes
import com.looker.kenko.ui.theme.colorSchemes.defaultColorSchemes
import com.looker.kenko.ui.theme.dynamicColorSchemes
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repo: SettingsRepo,
    context: Context,
) : ViewModel() {

    val theme: StateFlow<Theme> = repo.get { theme }
        .asStateFlow(Theme.System)

    val colorScheme: StateFlow<ColorSchemes> = repo.get { colorPalette }
        .map { it.scheme ?: dynamicColorSchemes(context) ?: defaultColorSchemes }
        .asStateFlow(defaultColorSchemes)

    val isOnboardingDone: Boolean = runBlocking { repo.get { isOnboardingDone }.first() }
}
