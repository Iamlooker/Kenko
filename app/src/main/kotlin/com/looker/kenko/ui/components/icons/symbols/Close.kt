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

val Close: ImageVector
    get() {
        if (_close != null) {
            return _close!!
        }
        _close = icon(
            name = "Close",
            viewPort = 960.0F to 960.0F,
            size = 24.dp to 24.dp,
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(480.0f, 522.15f)
                lineTo(277.08f, 725.08f)
                quadTo(268.77f, 733.38f, 256.19f, 733.58f)
                quadTo(243.62f, 733.77f, 234.92f, 725.08f)
                quadTo(226.23f, 716.38f, 226.23f, 704.0f)
                quadTo(226.23f, 691.62f, 234.92f, 682.92f)
                lineTo(437.85f, 480.0f)
                lineTo(234.92f, 277.08f)
                quadTo(226.62f, 268.77f, 226.42f, 256.19f)
                quadTo(226.23f, 243.62f, 234.92f, 234.92f)
                quadTo(243.62f, 226.23f, 256.0f, 226.23f)
                quadTo(268.38f, 226.23f, 277.08f, 234.92f)
                lineTo(480.0f, 437.85f)
                lineTo(682.92f, 234.92f)
                quadTo(691.23f, 226.62f, 703.81f, 226.42f)
                quadTo(716.38f, 226.23f, 725.08f, 234.92f)
                quadTo(733.77f, 243.62f, 733.77f, 256.0f)
                quadTo(733.77f, 268.38f, 725.08f, 277.08f)
                lineTo(522.15f, 480.0f)
                lineTo(725.08f, 682.92f)
                quadTo(733.38f, 691.23f, 733.58f, 703.81f)
                quadTo(733.77f, 716.38f, 725.08f, 725.08f)
                quadTo(716.38f, 733.77f, 704.0f, 733.77f)
                quadTo(691.62f, 733.77f, 682.92f, 725.08f)
                lineTo(480.0f, 522.15f)
                close()
            }
        }
        return _close!!
    }

private var _close: ImageVector? = null
