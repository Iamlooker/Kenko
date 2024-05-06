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
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(240.0f, 760.0f)
                lineTo(360.0f, 760.0f)
                lineTo(360.0f, 560.0f)
                quadTo(360.0f, 543.0f, 371.5f, 531.5f)
                quadTo(383.0f, 520.0f, 400.0f, 520.0f)
                lineTo(560.0f, 520.0f)
                quadTo(577.0f, 520.0f, 588.5f, 531.5f)
                quadTo(600.0f, 543.0f, 600.0f, 560.0f)
                lineTo(600.0f, 760.0f)
                lineTo(720.0f, 760.0f)
                lineTo(720.0f, 400.0f)
                quadTo(720.0f, 400.0f, 720.0f, 400.0f)
                quadTo(720.0f, 400.0f, 720.0f, 400.0f)
                lineTo(480.0f, 220.0f)
                quadTo(480.0f, 220.0f, 480.0f, 220.0f)
                quadTo(480.0f, 220.0f, 480.0f, 220.0f)
                lineTo(240.0f, 400.0f)
                quadTo(240.0f, 400.0f, 240.0f, 400.0f)
                quadTo(240.0f, 400.0f, 240.0f, 400.0f)
                lineTo(240.0f, 760.0f)
                close()
                moveTo(160.0f, 760.0f)
                lineTo(160.0f, 400.0f)
                quadTo(160.0f, 381.0f, 168.5f, 364.0f)
                quadTo(177.0f, 347.0f, 192.0f, 336.0f)
                lineTo(432.0f, 156.0f)
                quadTo(453.0f, 140.0f, 480.0f, 140.0f)
                quadTo(507.0f, 140.0f, 528.0f, 156.0f)
                lineTo(768.0f, 336.0f)
                quadTo(783.0f, 347.0f, 791.5f, 364.0f)
                quadTo(800.0f, 381.0f, 800.0f, 400.0f)
                lineTo(800.0f, 760.0f)
                quadTo(800.0f, 793.0f, 776.5f, 816.5f)
                quadTo(753.0f, 840.0f, 720.0f, 840.0f)
                lineTo(560.0f, 840.0f)
                quadTo(543.0f, 840.0f, 531.5f, 828.5f)
                quadTo(520.0f, 817.0f, 520.0f, 800.0f)
                lineTo(520.0f, 600.0f)
                quadTo(520.0f, 600.0f, 520.0f, 600.0f)
                quadTo(520.0f, 600.0f, 520.0f, 600.0f)
                lineTo(440.0f, 600.0f)
                quadTo(440.0f, 600.0f, 440.0f, 600.0f)
                quadTo(440.0f, 600.0f, 440.0f, 600.0f)
                lineTo(440.0f, 800.0f)
                quadTo(440.0f, 817.0f, 428.5f, 828.5f)
                quadTo(417.0f, 840.0f, 400.0f, 840.0f)
                lineTo(240.0f, 840.0f)
                quadTo(207.0f, 840.0f, 183.5f, 816.5f)
                quadTo(160.0f, 793.0f, 160.0f, 760.0f)
                close()
                moveTo(480.0f, 490.0f)
                lineTo(480.0f, 490.0f)
                lineTo(480.0f, 490.0f)
                quadTo(480.0f, 490.0f, 480.0f, 490.0f)
                quadTo(480.0f, 490.0f, 480.0f, 490.0f)
                lineTo(480.0f, 490.0f)
                lineTo(480.0f, 490.0f)
                lineTo(480.0f, 490.0f)
                lineTo(480.0f, 490.0f)
                quadTo(480.0f, 490.0f, 480.0f, 490.0f)
                quadTo(480.0f, 490.0f, 480.0f, 490.0f)
                lineTo(480.0f, 490.0f)
                quadTo(480.0f, 490.0f, 480.0f, 490.0f)
                quadTo(480.0f, 490.0f, 480.0f, 490.0f)
                lineTo(480.0f, 490.0f)
                lineTo(480.0f, 490.0f)
                close()
            }
        }
        return _home!!
    }

private var _home: ImageVector? = null
