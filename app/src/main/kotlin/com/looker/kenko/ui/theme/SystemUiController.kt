package com.looker.kenko.ui.theme

import android.app.Activity
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun rememberSystemUiController(): SystemUiController? {
    val view = LocalView.current
    return remember {
        if (!view.isInEditMode) {
            SystemUiController(view)
        } else {
//            error("Cannot set inset colors in edit mode")
            null
        }
    }
}

@Stable
class SystemUiController(
    view: View,
) {

    private val window = (view.context as Activity).window

    private val controller = WindowCompat.getInsetsController(window, view)

    fun colors(color: Int) {
        window.statusBarColor = color
        window.navigationBarColor = color
    }

    fun isLightStatusBar(value: Boolean) {
        controller.isAppearanceLightStatusBars = value
    }

    fun isLightNavigationBar(value: Boolean) {
        controller.isAppearanceLightNavigationBars = value
    }

    fun isLightSystemBar(value: Boolean) {
        isLightNavigationBar(value)
        isLightStatusBar(value)
    }
}
