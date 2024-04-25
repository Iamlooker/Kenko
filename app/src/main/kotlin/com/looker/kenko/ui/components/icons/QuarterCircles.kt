package com.looker.kenko.ui.components.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path

val QuarterCircles: ImageVector
    get() {
        if (_quarterCircles != null) {
            return _quarterCircles!!
        }
        _quarterCircles = icon(
            name = "QuarterCircles",
            viewPort = 246F to 311F,
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(247.924f, 192.73f)
                lineTo(247.136f, 63.733f)
                lineTo(118.115f, 65.16f)
                curveTo(118.348f, 99.402f, 131.87f, 130.272f, 153.731f, 153.174f)
                lineTo(25.528f, 154.57f)
                curveTo(26.027f, 225.81f, 84.129f, 282.901f, 155.424f, 282.141f)
                lineTo(154.533f, 154.004f)
                curveTo(178.177f, 178.225f, 211.308f, 193.116f, 247.924f, 192.73f)
                close()
            }
        }
        return _quarterCircles!!
    }

private var _quarterCircles: ImageVector? = null
