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

val Delete: ImageVector
    get() {
        if (_delete != null) {
            return _delete!!
        }
        _delete = icon(
            name = "Delete",
            viewPort = 960.0F to 960.0F,
            size = 24.dp to 24.dp,
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(292.31f, 820.0f)
                quadTo(262.48f, 820.0f, 241.24f, 798.76f)
                quadTo(220.0f, 777.52f, 220.0f, 747.69f)
                lineTo(220.0f, 240.0f)
                lineTo(210.0f, 240.0f)
                quadTo(197.25f, 240.0f, 188.63f, 231.37f)
                quadTo(180.0f, 222.74f, 180.0f, 209.99f)
                quadTo(180.0f, 197.23f, 188.63f, 188.62f)
                quadTo(197.25f, 180.0f, 210.0f, 180.0f)
                lineTo(360.0f, 180.0f)
                lineTo(360.0f, 180.0f)
                quadTo(360.0f, 165.31f, 370.35f, 154.96f)
                quadTo(380.69f, 144.62f, 395.38f, 144.62f)
                lineTo(564.62f, 144.62f)
                quadTo(579.31f, 144.62f, 589.65f, 154.96f)
                quadTo(600.0f, 165.31f, 600.0f, 180.0f)
                lineTo(600.0f, 180.0f)
                lineTo(750.0f, 180.0f)
                quadTo(762.75f, 180.0f, 771.37f, 188.63f)
                quadTo(780.0f, 197.26f, 780.0f, 210.01f)
                quadTo(780.0f, 222.77f, 771.37f, 231.38f)
                quadTo(762.75f, 240.0f, 750.0f, 240.0f)
                lineTo(740.0f, 240.0f)
                lineTo(740.0f, 747.69f)
                quadTo(740.0f, 777.52f, 718.76f, 798.76f)
                quadTo(697.52f, 820.0f, 667.69f, 820.0f)
                lineTo(292.31f, 820.0f)
                close()
                moveTo(680.0f, 240.0f)
                lineTo(280.0f, 240.0f)
                lineTo(280.0f, 747.69f)
                quadTo(280.0f, 753.08f, 283.46f, 756.54f)
                quadTo(286.92f, 760.0f, 292.31f, 760.0f)
                lineTo(667.69f, 760.0f)
                quadTo(673.08f, 760.0f, 676.54f, 756.54f)
                quadTo(680.0f, 753.08f, 680.0f, 747.69f)
                lineTo(680.0f, 240.0f)
                close()
                moveTo(406.17f, 680.0f)
                quadTo(418.92f, 680.0f, 427.54f, 671.38f)
                quadTo(436.15f, 662.75f, 436.15f, 650.0f)
                lineTo(436.15f, 350.0f)
                quadTo(436.15f, 337.25f, 427.52f, 328.62f)
                quadTo(418.9f, 320.0f, 406.14f, 320.0f)
                quadTo(393.39f, 320.0f, 384.77f, 328.62f)
                quadTo(376.16f, 337.25f, 376.16f, 350.0f)
                lineTo(376.16f, 650.0f)
                quadTo(376.16f, 662.75f, 384.78f, 671.38f)
                quadTo(393.41f, 680.0f, 406.17f, 680.0f)
                close()
                moveTo(553.86f, 680.0f)
                quadTo(566.61f, 680.0f, 575.23f, 671.38f)
                quadTo(583.84f, 662.75f, 583.84f, 650.0f)
                lineTo(583.84f, 350.0f)
                quadTo(583.84f, 337.25f, 575.22f, 328.62f)
                quadTo(566.59f, 320.0f, 553.83f, 320.0f)
                quadTo(541.08f, 320.0f, 532.46f, 328.62f)
                quadTo(523.85f, 337.25f, 523.85f, 350.0f)
                lineTo(523.85f, 650.0f)
                quadTo(523.85f, 662.75f, 532.48f, 671.38f)
                quadTo(541.1f, 680.0f, 553.86f, 680.0f)
                close()
                moveTo(280.0f, 240.0f)
                lineTo(280.0f, 240.0f)
                lineTo(280.0f, 747.69f)
                quadTo(280.0f, 753.08f, 280.0f, 756.54f)
                quadTo(280.0f, 760.0f, 280.0f, 760.0f)
                lineTo(280.0f, 760.0f)
                quadTo(280.0f, 760.0f, 280.0f, 756.54f)
                quadTo(280.0f, 753.08f, 280.0f, 747.69f)
                lineTo(280.0f, 240.0f)
                close()
            }
        }
        return _delete!!
    }

private var _delete: ImageVector? = null
