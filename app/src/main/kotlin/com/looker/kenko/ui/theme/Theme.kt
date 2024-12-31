package com.looker.kenko.ui.theme

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.looker.kenko.R
import com.looker.kenko.data.model.settings.Theme
import com.looker.kenko.ui.theme.colorSchemes.ColorSchemes
import com.looker.kenko.ui.theme.colorSchemes.defaultColorSchemes

@Composable
fun KenkoTheme(
    theme: Theme = Theme.System,
    colorSchemes: ColorSchemes = defaultColorSchemes,
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

    SetupInsets(isDarkTheme = isDarkTheme)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
private fun SetupInsets(
    isDarkTheme: Boolean,
    insetColor: Color = Color.Transparent,
) {
    val controller = rememberSystemUiController()
    SideEffect {
        controller?.colors(insetColor.toArgb())
        controller?.isLightSystemBar(!isDarkTheme)
    }
}

fun dynamicColorSchemes(context: Context): ColorSchemes? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ColorSchemes(
            light = dynamicLightColorScheme(context),
            dark = dynamicDarkColorScheme(context),
            nameRes = R.string.label_color_scheme_dynamic
        )
    } else {
        null
    }
