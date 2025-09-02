/*
 * Copyright (C) 2025. LooKeR & Contributors
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
    axisStrokePadding: Float,
    gridColor: Color,
    gridStroke: Float = 2F,
    verticalGridCount: Int = 4,
    horizontalGridCount: Int = 3,
) {
    val left = axisStrokePadding
    val right = size.width - axisStrokePadding
    val top = axisStrokePadding
    val bottom = size.height - axisStrokePadding
    val gridWidth = right - left
    val gridHeight = bottom - top
    if (gridWidth > 0f && gridHeight > 0f) {
        // Vertical grid lines
        if (verticalGridCount > 0) {
            val dx = gridWidth / (verticalGridCount + 1)
            var i = 1
            while (i <= verticalGridCount) {
                val x = left + i * dx
                drawLine(
                    color = gridColor,
                    start = Offset(x, top),
                    end = Offset(x, bottom),
                    strokeWidth = gridStroke,
                )
                i++
            }
        }
        // Horizontal grid lines
        if (horizontalGridCount > 0) {
            val dy = gridHeight / (horizontalGridCount + 1)
            var j = 1
            while (j <= horizontalGridCount) {
                val y = top + j * dy
                drawLine(
                    color = gridColor,
                    start = Offset(left, y),
                    end = Offset(right, y),
                    strokeWidth = gridStroke,
                )
                j++
            }
        }
    }
}