package com.looker.kenko.ui.settings

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.data.model.settings.ColorPalettes
import com.looker.kenko.data.model.settings.Theme
import com.looker.kenko.data.repository.SettingsRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: SettingsRepo,
) : ViewModel() {

    val state: StateFlow<SettingsUiData> = combine(
        repo.get { theme },
        repo.get { colorPalette }
    ) { theme, colorPalette ->
        SettingsUiData(theme, colorPalette)
    }.asStateFlow(SettingsUiData(Theme.System, ColorPalettes.Default))

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
}

@Stable
data class SettingsUiData(
    val selectedTheme: Theme,
    val selectedColorPalette: ColorPalettes,
)
