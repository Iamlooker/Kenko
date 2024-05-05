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

val Lightbulb: ImageVector
    get() {
        if (_lightbulb != null) {
            return _lightbulb!!
        }
        _lightbulb = icon(
            name = "Lightbulb",
            viewPort = 960.0F to 960.0F,
            size = 24.dp to 24.dp,
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(480.0f, 863.08f)
                quadTo(449.69f, 863.08f, 427.73f, 842.08f)
                quadTo(405.77f, 821.08f, 403.85f, 790.77f)
                lineTo(556.15f, 790.77f)
                quadTo(554.23f, 821.08f, 532.27f, 842.08f)
                quadTo(510.31f, 863.08f, 480.0f, 863.08f)
                close()
                moveTo(360.0f, 735.38f)
                quadTo(347.23f, 735.38f, 338.62f, 726.77f)
                quadTo(330.0f, 718.15f, 330.0f, 705.38f)
                quadTo(330.0f, 692.61f, 338.62f, 684.0f)
                quadTo(347.23f, 675.38f, 360.0f, 675.38f)
                lineTo(600.0f, 675.38f)
                quadTo(612.77f, 675.38f, 621.38f, 684.0f)
                quadTo(630.0f, 692.61f, 630.0f, 705.38f)
                quadTo(630.0f, 718.15f, 621.38f, 726.77f)
                quadTo(612.77f, 735.38f, 600.0f, 735.38f)
                lineTo(360.0f, 735.38f)
                close()
                moveTo(336.15f, 620.0f)
                quadTo(273.31f, 580.92f, 236.66f, 517.88f)
                quadTo(200.0f, 454.85f, 200.0f, 380.0f)
                quadTo(200.0f, 263.08f, 281.54f, 181.54f)
                quadTo(363.08f, 100.0f, 480.0f, 100.0f)
                quadTo(596.92f, 100.0f, 678.46f, 181.54f)
                quadTo(760.0f, 263.08f, 760.0f, 380.0f)
                quadTo(760.0f, 454.85f, 723.34f, 517.88f)
                quadTo(686.69f, 580.92f, 623.85f, 620.0f)
                lineTo(336.15f, 620.0f)
                close()
                moveTo(354.0f, 560.0f)
                lineTo(606.0f, 560.0f)
                quadTo(651.0f, 528.0f, 675.5f, 481.0f)
                quadTo(700.0f, 434.0f, 700.0f, 380.0f)
                quadTo(700.0f, 288.0f, 636.0f, 224.0f)
                quadTo(572.0f, 160.0f, 480.0f, 160.0f)
                quadTo(388.0f, 160.0f, 324.0f, 224.0f)
                quadTo(260.0f, 288.0f, 260.0f, 380.0f)
                quadTo(260.0f, 434.0f, 284.5f, 481.0f)
                quadTo(309.0f, 528.0f, 354.0f, 560.0f)
                close()
                moveTo(480.0f, 560.0f)
                quadTo(480.0f, 560.0f, 480.0f, 560.0f)
                quadTo(480.0f, 560.0f, 480.0f, 560.0f)
                quadTo(480.0f, 560.0f, 480.0f, 560.0f)
                quadTo(480.0f, 560.0f, 480.0f, 560.0f)
                quadTo(480.0f, 560.0f, 480.0f, 560.0f)
                quadTo(480.0f, 560.0f, 480.0f, 560.0f)
                quadTo(480.0f, 560.0f, 480.0f, 560.0f)
                quadTo(480.0f, 560.0f, 480.0f, 560.0f)
                lineTo(480.0f, 560.0f)
                close()
            }
        }
        return _lightbulb!!
    }

private var _lightbulb: ImageVector? = null
