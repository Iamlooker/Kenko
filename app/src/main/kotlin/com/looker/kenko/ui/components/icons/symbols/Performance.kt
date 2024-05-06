package com.looker.kenko.ui.components.icons.symbols

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
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
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(110.0f, 710.0f)
                quadTo(97.0f, 697.0f, 97.0f, 680.0f)
                quadTo(97.0f, 663.0f, 110.0f, 650.0f)
                lineTo(323.0f, 437.0f)
                quadTo(346.0f, 414.0f, 380.0f, 414.0f)
                quadTo(414.0f, 414.0f, 437.0f, 437.0f)
                lineTo(540.0f, 540.0f)
                quadTo(540.0f, 540.0f, 540.0f, 540.0f)
                quadTo(540.0f, 540.0f, 540.0f, 540.0f)
                lineTo(796.0f, 251.0f)
                quadTo(807.0f, 238.0f, 824.5f, 238.0f)
                quadTo(842.0f, 238.0f, 854.0f, 250.0f)
                quadTo(865.0f, 261.0f, 865.5f, 276.5f)
                quadTo(866.0f, 292.0f, 855.0f, 304.0f)
                lineTo(596.0f, 596.0f)
                quadTo(573.0f, 622.0f, 539.0f, 623.5f)
                quadTo(505.0f, 625.0f, 480.0f, 600.0f)
                lineTo(380.0f, 500.0f)
                quadTo(380.0f, 500.0f, 380.0f, 500.0f)
                quadTo(380.0f, 500.0f, 380.0f, 500.0f)
                lineTo(170.0f, 710.0f)
                quadTo(157.0f, 723.0f, 140.0f, 723.0f)
                quadTo(123.0f, 723.0f, 110.0f, 710.0f)
                close()
            }
        }
        return _performance!!
    }

private var _performance: ImageVector? = null
