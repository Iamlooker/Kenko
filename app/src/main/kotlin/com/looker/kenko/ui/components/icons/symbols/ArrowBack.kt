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

val ArrowBack: ImageVector
    get() {
        if (_arrowBack != null) {
            return _arrowBack!!
        }
        _arrowBack = icon(
            name = "Add",
            viewPort = 960.0F to 960.0F,
            size = 24.dp to 24.dp,
            autoMirror = true,
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(294.92f, 510.0f)
                lineTo(501.69f, 716.77f)
                quadTo(510.61f, 725.69f, 510.5f, 737.65f)
                quadTo(510.38f, 749.61f, 501.08f, 758.92f)
                quadTo(491.77f, 767.61f, 480.0f, 767.92f)
                quadTo(468.23f, 768.23f, 458.92f, 758.92f)
                lineTo(205.31f, 505.31f)
                quadTo(199.69f, 499.69f, 197.39f, 493.46f)
                quadTo(195.08f, 487.23f, 195.08f, 480.0f)
                quadTo(195.08f, 472.77f, 197.39f, 466.54f)
                quadTo(199.69f, 460.31f, 205.31f, 454.69f)
                lineTo(458.92f, 201.08f)
                quadTo(467.23f, 192.77f, 479.5f, 192.58f)
                quadTo(491.77f, 192.39f, 501.08f, 201.08f)
                quadTo(510.38f, 210.39f, 510.38f, 222.46f)
                quadTo(510.38f, 234.54f, 501.08f, 243.85f)
                lineTo(294.92f, 450.0f)
                lineTo(750.0f, 450.0f)
                quadTo(762.77f, 450.0f, 771.38f, 458.62f)
                quadTo(780.0f, 467.23f, 780.0f, 480.0f)
                quadTo(780.0f, 492.77f, 771.38f, 501.38f)
                quadTo(762.77f, 510.0f, 750.0f, 510.0f)
                lineTo(294.92f, 510.0f)
                close()
            }
        }
        return _arrowBack!!
    }

private var _arrowBack: ImageVector? = null
