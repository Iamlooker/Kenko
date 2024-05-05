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

val ArrowOutward: ImageVector
    get() {
        if (_arrowOutward != null) {
            return _arrowOutward!!
        }
        _arrowOutward = icon(
            name = "ArrowOutward",
            viewPort = 960.0F to 960.0F,
            size = 24.dp to 24.dp,
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(645.77f, 312.15f)
                lineTo(272.46f, 685.08f)
                quadTo(264.15f, 693.38f, 251.58f, 693.19f)
                quadTo(239.0f, 693.0f, 230.69f, 684.69f)
                quadTo(222.39f, 676.38f, 222.39f, 664.0f)
                quadTo(222.39f, 651.62f, 230.69f, 643.31f)
                lineTo(603.62f, 270.0f)
                lineTo(275.77f, 270.0f)
                quadTo(263.02f, 270.0f, 254.39f, 261.37f)
                quadTo(245.77f, 252.74f, 245.77f, 239.99f)
                quadTo(245.77f, 227.23f, 254.39f, 218.62f)
                quadTo(263.02f, 210.0f, 275.77f, 210.0f)
                lineTo(669.61f, 210.0f)
                quadTo(684.98f, 210.0f, 695.37f, 220.39f)
                quadTo(705.77f, 230.79f, 705.77f, 246.15f)
                lineTo(705.77f, 640.0f)
                quadTo(705.77f, 652.75f, 697.14f, 661.37f)
                quadTo(688.51f, 670.0f, 675.76f, 670.0f)
                quadTo(663.0f, 670.0f, 654.38f, 661.37f)
                quadTo(645.77f, 652.75f, 645.77f, 640.0f)
                lineTo(645.77f, 312.15f)
                close()
            }
        }
        return _arrowOutward!!
    }

private var _arrowOutward: ImageVector? = null
