package com.looker.kenko.ui.components.icons.symbols

import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.components.icons.icon

val Performance: ImageVector
    get() {
        if (_performance != null) {
            return _performance!!
        }
        _performance = icon(
            name = "Performance",
            viewPort = 960.0F to 960.0F,
            size = 24.dp to 24.dp,
        ) {
            materialPath {
                moveTo(140.0F, 726.15F)
                lineTo(95.39F, 681.54F)
                lineTo(381.54F, 395.39F)
                lineTo(541.54F, 555.39F)
                lineTo(823.23F, 235.39F)
                lineTo(865.38F, 276.77F)
                lineTo(542.31F, 645.38F)
                lineTo(381.54F, 484.61F)
                lineTo(140.0F, 726.15F)
                close()
            }
        }
        return _performance!!
    }

private var _performance: ImageVector? = null
