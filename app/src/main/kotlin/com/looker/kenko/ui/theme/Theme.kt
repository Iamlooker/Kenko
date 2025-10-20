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

package com.looker.kenko.ui.theme

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.looker.kenko.R
import com.looker.kenko.data.model.settings.Theme
import com.looker.kenko.ui.theme.colorSchemes.ColorSchemes
import com.looker.kenko.ui.theme.colorSchemes.zestfulColorSchemes

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun KenkoTheme(
    theme: Theme = Theme.System,
    colorSchemes: ColorSchemes = zestfulColorSchemes,
    content: @Composable () -> Unit,
) {
    val systemTheme = isSystemInDarkTheme()
    val isDarkTheme = remember(theme) {
        when (theme) {
            Theme.System -> systemTheme
            Theme.Light -> false
            Theme.Dark -> true
        }
    }
    val colorScheme = if (isDarkTheme) {
        colorSchemes.dark
    } else {
        colorSchemes.light
    }

    val localView = LocalView.current
    SideEffect { setupSystemBar(localView, isDarkTheme) }

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}

fun setupSystemBar(view: View, isDarkTheme: Boolean) {
    if (view.isInEditMode) return
    val window = (view.context as Activity).window
    with(WindowCompat.getInsetsController(window, view)) {
        isAppearanceLightStatusBars = !isDarkTheme
        isAppearanceLightNavigationBars = !isDarkTheme
    }
}

fun dynamicColorSchemes(context: Context): ColorSchemes? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ColorSchemes(
            light = dynamicLightColorScheme(context),
            dark = dynamicDarkColorScheme(context),
            nameRes = R.string.label_color_scheme_dynamic,
        )
    } else {
        null
    }
