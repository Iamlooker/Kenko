package com.looker.kenko.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.looker.kenko.data.model.settings.Settings
import com.looker.kenko.data.model.settings.Theme
import com.looker.kenko.data.repository.SettingsRepo
import com.looker.kenko.ui.theme.colorSchemes.ColorSchemes
import com.looker.kenko.ui.theme.colorSchemes.defaultColorSchemes
import com.looker.kenko.ui.theme.dynamicColorSchemes
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repo: SettingsRepo,
    context: Context,
) : ViewModel() {

    private val stream: Flow<Settings> = repo.stream

    val theme: StateFlow<Theme> = stream.map {
        it.theme
    }.asStateFlow(Theme.System)

    val colorScheme: StateFlow<ColorSchemes> = stream.map {
        it.colorPalette.scheme ?: dynamicColorSchemes(context) ?: defaultColorSchemes
    }.asStateFlow(defaultColorSchemes)

}