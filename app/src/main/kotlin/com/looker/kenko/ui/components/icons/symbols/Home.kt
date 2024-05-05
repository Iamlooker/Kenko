package com.looker.kenko.ui.components.icons.symbols

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.components.icons.icon

val Home: ImageVector
    get() {
        if (_home != null) {
            return _home!!
        }
        _home = icon(
            name = "Home",
            viewPort = 960.0F to 960.0F,
            size = 24.dp to 24.dp,
        ) {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0F,
                strokeAlpha = 1.0F,
                strokeLineWidth = 0.0F,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 4.0F,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(240.0F, 760.0F)
                lineTo(360.0F, 760.0F)
                lineTo(360.0F, 520.0F)
                lineTo(600.0F, 520.0F)
                lineTo(600.0F, 760.0F)
                lineTo(720.0F, 760.0F)
                lineTo(720.0F, 400.0F)
                lineTo(480.0F, 220.0F)
                lineTo(240.0F, 400.0F)
                lineTo(240.0F, 760.0F)

                moveTo(160.0F, 840.0F)
                lineTo(160.0F, 360.0F)
                lineTo(480.0F, 120.0F)
                lineTo(800.0F, 360.0F)
                lineTo(800.0F, 840.0F)
                lineTo(520.0F, 840.0F)
                lineTo(520.0F, 600.0F)
                lineTo(440.0F, 600.0F)
                lineTo(440.0F, 840.0F)
                lineTo(160.0F, 840.0F)

                moveTo(480.0F, 490.0F)
                lineTo(480.0F, 490.0F)
                lineTo(480.0F, 490.0F)
                lineTo(480.0F, 490.0F)
                lineTo(480.0F, 490.0F)
                lineTo(480.0F, 490.0F)
                lineTo(480.0F, 490.0F)
                lineTo(480.0F, 490.0F)
                lineTo(480.0F, 490.0F)
                lineTo(480.0F, 490.0F)
                close()
            }
        }
        return _home!!
    }
private var _home: ImageVector? = null
