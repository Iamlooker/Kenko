package com.looker.kenko.data.model.settings

import androidx.annotation.StringRes
import com.looker.kenko.R
import com.looker.kenko.ui.theme.colorSchemes.ColorSchemes
import com.looker.kenko.ui.theme.colorSchemes.defaultColorSchemes
import com.looker.kenko.ui.theme.colorSchemes.sereneColorSchemes
import com.looker.kenko.ui.theme.colorSchemes.twilightColorSchemes
import com.looker.kenko.ui.theme.colorSchemes.zestfulColorSchemes

enum class Theme(@StringRes val nameRes: Int) {
    System(R.string.label_theme_system),
    Light(R.string.label_theme_light),
    Dark(R.string.label_theme_dark),
}

enum class ColorPalettes(val scheme: ColorSchemes?) {
    Dynamic(null),
    Default(defaultColorSchemes),
    Zestful(zestfulColorSchemes),
    Serene(sereneColorSchemes),
    Twilight(twilightColorSchemes),
}
