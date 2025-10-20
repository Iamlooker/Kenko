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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ArrowOutwardLarge: ImageVector
    get() {
        if (_arrowOutwardLarge != null) {
            return _arrowOutwardLarge!!
        }
        _arrowOutwardLarge = icon(
            name = "ArrowOutwardLarge",
            viewPort = 960.0F to 960.0F,
            size = 120.dp to 120.dp,
        ) {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 12.0f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 0.0f,
                pathFillType = NonZero
            ) {
                moveTo(645.77f, 312.15f)
                lineTo(272.46f, 685.08f)
                quadTo(264.15f, 693.38f, 251.58f, 693.19f)
                quadTo(239.0f, 693.0f, 230.69f, 684.69f)
                quadTo(222.39f, 676.38f, 222.39f, 664.0f)
                quadTo(222.39f, 651.62f, 230.69f, 643.31f)
                lineTo(603.62f, 270.0f)
                lineTo(275.77f, 270.0f)
                quadTo(263.02f, 270.0f, 254.39f, 261.37f)
                quadTo(245.77f, 252.74f, 245.77f, 239.99f)
                quadTo(245.77f, 227.23f, 254.39f, 218.62f)
                quadTo(263.02f, 210.0f, 275.77f, 210.0f)
                lineTo(669.61f, 210.0f)
                quadTo(684.98f, 210.0f, 695.37f, 220.39f)
                quadTo(705.77f, 230.79f, 705.77f, 246.15f)
                lineTo(705.77f, 640.0f)
                quadTo(705.77f, 652.75f, 697.14f, 661.37f)
                quadTo(688.51f, 670.0f, 675.76f, 670.0f)
                quadTo(663.0f, 670.0f, 654.38f, 661.37f)
                quadTo(645.77f, 652.75f, 645.77f, 640.0f)
                lineTo(645.77f, 312.15f)
                close()
            }
        }
        return _arrowOutwardLarge!!
    }

private var _arrowOutwardLarge: ImageVector? = null
