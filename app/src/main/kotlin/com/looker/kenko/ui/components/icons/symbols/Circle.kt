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

val Circle: ImageVector
    get() {
        if (_circle != null) {
            return _circle!!
        }
        _circle = icon(
            name = "Circle",
            viewPort = 960.0F to 960.0F,
            size = 24.dp to 24.dp,
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(480.0f, 480.0f)
                quadTo(480.0f, 480.0f, 480.0f, 480.0f)
                quadTo(480.0f, 480.0f, 480.0f, 480.0f)
                quadTo(480.0f, 480.0f, 480.0f, 480.0f)
                quadTo(480.0f, 480.0f, 480.0f, 480.0f)
                quadTo(480.0f, 480.0f, 480.0f, 480.0f)
                quadTo(480.0f, 480.0f, 480.0f, 480.0f)
                quadTo(480.0f, 480.0f, 480.0f, 480.0f)
                quadTo(480.0f, 480.0f, 480.0f, 480.0f)
                close()
                moveTo(480.0f, 760.0f)
                quadTo(364.0f, 760.0f, 282.0f, 678.0f)
                quadTo(200.0f, 596.0f, 200.0f, 480.0f)
                quadTo(200.0f, 364.0f, 282.0f, 282.0f)
                quadTo(364.0f, 200.0f, 480.0f, 200.0f)
                quadTo(596.0f, 200.0f, 678.0f, 282.0f)
                quadTo(760.0f, 364.0f, 760.0f, 480.0f)
                quadTo(760.0f, 596.0f, 678.0f, 678.0f)
                quadTo(596.0f, 760.0f, 480.0f, 760.0f)
                close()
                moveTo(480.0f, 680.0f)
                quadTo(563.0f, 680.0f, 621.5f, 621.5f)
                quadTo(680.0f, 563.0f, 680.0f, 480.0f)
                quadTo(680.0f, 397.0f, 621.5f, 338.5f)
                quadTo(563.0f, 280.0f, 480.0f, 280.0f)
                quadTo(397.0f, 280.0f, 338.5f, 338.5f)
                quadTo(280.0f, 397.0f, 280.0f, 480.0f)
                quadTo(280.0f, 563.0f, 338.5f, 621.5f)
                quadTo(397.0f, 680.0f, 480.0f, 680.0f)
                close()
            }
        }
        return _circle!!
    }

private var _circle: ImageVector? = null
