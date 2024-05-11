package com.looker.kenko.data.model.settings

import com.looker.kenko.ui.theme.colorSchemes.ColorSchemes
import com.looker.kenko.ui.theme.colorSchemes.defaultColorSchemes
import com.looker.kenko.ui.theme.colorSchemes.sereneColorSchemes
import com.looker.kenko.ui.theme.colorSchemes.twilightColorSchemes
import com.looker.kenko.ui.theme.colorSchemes.zestfulColorSchemes

enum class Theme {
    System,
    Light,
    Dark,
}

enum class ColorPalettes(val scheme: ColorSchemes?) {
    Dynamic(null),
    Default(defaultColorSchemes),
    Zestful(zestfulColorSchemes),
    Serene(sereneColorSchemes),
    Twilight(twilightColorSchemes),
}
