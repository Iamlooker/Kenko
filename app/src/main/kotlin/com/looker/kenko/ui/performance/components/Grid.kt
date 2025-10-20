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

package com.looker.kenko.ui.performance.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.drawGrid(
    gridColor: Color,
    gridStroke: Float = 2F,
    verticalGridCount: Int = 4,
    horizontalGridCount: Int = 3,
) {
    if (verticalGridCount > 0) {
        val dx = size.width / (verticalGridCount + 1)
        for (i in 1..verticalGridCount) {
            val x = i * dx
            drawLine(
                color = gridColor,
                start = Offset(x, 0F),
                end = Offset(x, size.height),
                strokeWidth = gridStroke,
            )
        }
    }

    if (horizontalGridCount > 0) {
        val dy = size.height / (horizontalGridCount + 1)
        for (j in 1..horizontalGridCount) {
            val y = j * dy
            drawLine(
                color = gridColor,
                start = Offset(0F, y),
                end = Offset(size.width, y),
                strokeWidth = gridStroke,
            )
        }
    }
}