/*
 * Copyright (C) 2025 LooKeR & Contributors
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.looker.kenko.ui.components.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val QuarterCircles: ImageVector
    get() {
        if (_quarterCircles != null) {
            return _quarterCircles!!
        }
        _quarterCircles = icon(
            name = "QuarterCircles",
            viewPort = 246F to 311F,
            size = 180.dp to 228.dp,
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
