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

val Edit: ImageVector
    get() {
        if (_edit != null) {
            return _edit!!
        }
        _edit = icon(
            name = "Edit",
            viewPort = 960.0F to 960.0F,
            size = 24.dp to 24.dp,
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(200.0f, 760.0f)
                lineTo(250.46f, 760.0f)
                lineTo(659.92f, 350.54f)
                lineTo(609.46f, 300.08f)
                lineTo(200.0f, 709.54f)
                lineTo(200.0f, 760.0f)
                close()
                moveTo(176.16f, 820.0f)
                quadTo(160.79f, 820.0f, 150.4f, 809.6f)
                quadTo(140.0f, 799.21f, 140.0f, 783.84f)
                lineTo(140.0f, 714.54f)
                quadTo(140.0f, 699.91f, 145.62f, 686.65f)
                quadTo(151.23f, 673.39f, 161.08f, 663.54f)
                lineTo(667.62f, 157.23f)
                quadTo(676.69f, 148.99f, 687.65f, 144.5f)
                quadTo(698.62f, 140.0f, 710.65f, 140.0f)
                quadTo(722.68f, 140.0f, 733.95f, 144.27f)
                quadTo(745.23f, 148.54f, 753.92f, 157.85f)
                lineTo(802.77f, 207.31f)
                quadTo(812.08f, 216.0f, 816.04f, 227.31f)
                quadTo(820.0f, 238.62f, 820.0f, 249.93f)
                quadTo(820.0f, 262.0f, 815.88f, 272.96f)
                quadTo(811.76f, 283.93f, 802.77f, 293.0f)
                lineTo(296.46f, 798.92f)
                quadTo(286.61f, 808.77f, 273.35f, 814.38f)
                quadTo(260.09f, 820.0f, 245.46f, 820.0f)
                lineTo(176.16f, 820.0f)
                close()
                moveTo(760.38f, 249.85f)
                lineTo(760.38f, 249.85f)
                lineTo(710.15f, 199.62f)
                lineTo(710.15f, 199.62f)
                lineTo(760.38f, 249.85f)
                close()
                moveTo(634.25f, 325.75f)
                lineTo(609.46f, 300.08f)
                lineTo(609.46f, 300.08f)
                lineTo(659.92f, 350.54f)
                lineTo(659.92f, 350.54f)
                lineTo(634.25f, 325.75f)
                close()
            }
        }
        return _edit!!
    }

private var _edit: ImageVector? = null
