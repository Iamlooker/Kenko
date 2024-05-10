package com.looker.kenko.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.looker.kenko.ui.theme.colorSchemes.ColorSchemes
import com.looker.kenko.ui.theme.colorSchemes.defaultColorSchemes

@Composable
fun KenkoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorSchemes: ColorSchemes = defaultColorSchemes,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> colorSchemes.dark
        else -> colorSchemes.light
    }
    SetupInsets(isDarkTheme = darkTheme)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
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
