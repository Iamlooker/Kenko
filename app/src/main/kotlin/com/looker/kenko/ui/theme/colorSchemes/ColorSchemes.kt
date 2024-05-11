package com.looker.kenko.ui.theme.colorSchemes

import androidx.annotation.StringRes
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable

@Immutable
data class ColorSchemes(
    val light: ColorScheme,
    val dark: ColorScheme,
    val mediumContrastLight: ColorScheme? = null,
    val mediumContrastDark: ColorScheme? = null,
    val highContrastLight: ColorScheme? = null,
    val highContrastDark: ColorScheme? = null,
    @StringRes val nameRes: Int,
)
