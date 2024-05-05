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

val ArrowForward: ImageVector
    get() {
        if (_arrowForward != null) {
            return _arrowForward!!
        }
        _arrowForward = icon(
            name = "ArrowForward",
            viewPort = 960.0F to 960.0F,
            size = 24.dp to 24.dp,
            autoMirror = true,
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(665.08f, 510.0f)
                lineTo(210.0f, 510.0f)
                quadTo(197.23f, 510.0f, 188.62f, 501.38f)
                quadTo(180.0f, 492.77f, 180.0f, 480.0f)
                quadTo(180.0f, 467.23f, 188.62f, 458.62f)
                quadTo(197.23f, 450.0f, 210.0f, 450.0f)
                lineTo(665.08f, 450.0f)
                lineTo(458.31f, 243.23f)
                quadTo(449.39f, 234.31f, 449.5f, 222.35f)
                quadTo(449.62f, 210.39f, 458.92f, 201.08f)
                quadTo(468.23f, 192.39f, 480.0f, 192.08f)
                quadTo(491.77f, 191.77f, 501.08f, 201.08f)
                lineTo(754.69f, 454.69f)
                quadTo(760.31f, 460.31f, 762.61f, 466.54f)
                quadTo(764.92f, 472.77f, 764.92f, 480.0f)
                quadTo(764.92f, 487.23f, 762.61f, 493.46f)
                quadTo(760.31f, 499.69f, 754.69f, 505.31f)
                lineTo(501.08f, 758.92f)
                quadTo(492.77f, 767.23f, 480.5f, 767.42f)
                quadTo(468.23f, 767.61f, 458.92f, 758.92f)
                quadTo(449.62f, 749.61f, 449.62f, 737.54f)
                quadTo(449.62f, 725.46f, 458.92f, 716.15f)
                lineTo(665.08f, 510.0f)
                close()
            }
        }
        return _arrowForward!!
    }

private var _arrowForward: ImageVector? = null
