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

val Reveal: ImageVector
    get() {
        if (_reveal != null) {
            return _reveal!!
        }
        _reveal = icon(
            name = "Reveal",
            viewPort = 374F to 394F,
            size = 125.dp to 131.dp,
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(187.0f, 187.6f)
                curveTo(290.277f, 187.6f, 374.0f, 145.783f, 374.0f, 94.2f)
                curveTo(374.0f, 42.616f, 290.277f, 0.8f, 187.0f, 0.8f)
                curveTo(83.723f, 0.8f, 0.0f, 42.616f, 0.0f, 94.2f)
                curveTo(0.0f, 145.783f, 83.723f, 187.6f, 187.0f, 187.6f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(187.0f, 298.0f)
                curveTo(290.277f, 298.0f, 374.0f, 273.286f, 374.0f, 242.8f)
                curveTo(374.0f, 212.313f, 290.277f, 187.6f, 187.0f, 187.6f)
                curveTo(83.723f, 187.6f, 0.0f, 212.313f, 0.0f, 242.8f)
                curveTo(0.0f, 273.286f, 83.723f, 298.0f, 187.0f, 298.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(187.0f, 361.4f)
                curveTo(290.277f, 361.4f, 374.0f, 347.207f, 374.0f, 329.7f)
                curveTo(374.0f, 312.193f, 290.277f, 298.0f, 187.0f, 298.0f)
                curveTo(83.723f, 298.0f, 0.0f, 312.193f, 0.0f, 329.7f)
                curveTo(0.0f, 347.207f, 83.723f, 361.4f, 187.0f, 361.4f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(187.0f, 393.2f)
                curveTo(290.277f, 393.2f, 374.0f, 386.081f, 374.0f, 377.3f)
                curveTo(374.0f, 368.519f, 290.277f, 361.4f, 187.0f, 361.4f)
                curveTo(83.723f, 361.4f, 0.0f, 368.519f, 0.0f, 377.3f)
                curveTo(0.0f, 386.081f, 83.723f, 393.2f, 187.0f, 393.2f)
                close()
            }
        }
        return _reveal!!
    }

private var _reveal: ImageVector? = null
