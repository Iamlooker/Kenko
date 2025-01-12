package com.looker.kenko.ui.theme

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}

fun setupSystemBar(view: View, isDarkTheme: Boolean) {
    if (view.isInEditMode) return
    val window = (view.context as Activity).window
    val controller = WindowCompat.getInsetsController(window, view)
    controller.isAppearanceLightStatusBars = !isDarkTheme
    controller.isAppearanceLightNavigationBars = !isDarkTheme
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
