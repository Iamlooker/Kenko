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

val AddLarge: ImageVector
    get() {
        if (_addLarge != null) {
            return _addLarge!!
        }
        _addLarge = icon(
            name = "AddLarge",
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
        return _addLarge!!
    }

private var _addLarge: ImageVector? = null
