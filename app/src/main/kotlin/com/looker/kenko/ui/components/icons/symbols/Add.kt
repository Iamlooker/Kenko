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

val Add: ImageVector
    get() {
        if (_add != null) {
            return _add!!
        }
        _add = icon(
            name = "Add",
            viewPort = 960.0F to 960.0F,
            size = 24.dp to 24.dp,
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(450.0f, 510.0f)
                lineTo(250.0f, 510.0f)
                quadTo(237.25f, 510.0f, 228.63f, 501.37f)
                quadTo(220.0f, 492.74f, 220.0f, 479.99f)
                quadTo(220.0f, 467.23f, 228.63f, 458.62f)
                quadTo(237.25f, 450.0f, 250.0f, 450.0f)
                lineTo(450.0f, 450.0f)
                lineTo(450.0f, 250.0f)
                quadTo(450.0f, 237.25f, 458.63f, 228.63f)
                quadTo(467.26f, 220.0f, 480.01f, 220.0f)
                quadTo(492.77f, 220.0f, 501.38f, 228.63f)
                quadTo(510.0f, 237.25f, 510.0f, 250.0f)
                lineTo(510.0f, 450.0f)
                lineTo(710.0f, 450.0f)
                quadTo(722.75f, 450.0f, 731.37f, 458.63f)
                quadTo(740.0f, 467.26f, 740.0f, 480.01f)
                quadTo(740.0f, 492.77f, 731.37f, 501.38f)
                quadTo(722.75f, 510.0f, 710.0f, 510.0f)
                lineTo(510.0f, 510.0f)
                lineTo(510.0f, 710.0f)
                quadTo(510.0f, 722.75f, 501.37f, 731.37f)
                quadTo(492.74f, 740.0f, 479.99f, 740.0f)
                quadTo(467.23f, 740.0f, 458.62f, 731.37f)
                quadTo(450.0f, 722.75f, 450.0f, 710.0f)
                lineTo(450.0f, 510.0f)
                close()
            }

        }
        return _add!!
    }

private var _add: ImageVector? = null
