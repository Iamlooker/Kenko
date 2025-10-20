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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.drawAxes(
    axesColor: Color,
    axisStroke: Float = 7F,
    strokeCap: StrokeCap = StrokeCap.Round,
) {
    val axisStrokePadding = axisStroke / 2

    drawLine(
        color = axesColor,
        start = Offset(axisStrokePadding, axisStrokePadding),
        end = Offset(axisStrokePadding, size.height - axisStrokePadding),
        strokeWidth = axisStroke,
        cap = strokeCap,
    )

    drawLine(
        color = axesColor,
        start = Offset(axisStrokePadding, size.height - axisStrokePadding),
        end = Offset(
            size.width - axisStrokePadding,
            size.height - axisStrokePadding
        ),
        strokeWidth = axisStroke,
        cap = strokeCap,
    )
}