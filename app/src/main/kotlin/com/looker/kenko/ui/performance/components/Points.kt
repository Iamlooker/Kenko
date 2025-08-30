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

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle

@Immutable
class Points(
    val points: FloatArray, // Only y points since x-axis is always aligned to start
    val stroke: Stroke,
    val pointColor: Color,
    val lineColor: Color,
    val gridColor: Color,
    val label: @Composable () -> Unit,
)

fun Points.toPath(size: Size): Path = Path().apply {
    val count = points.size
    if (count == 0) return@apply

    // Determine horizontal spacing so that first point starts at x=0 and last at x=width
    val stepX = if (count > 1) size.width / (count - 1) else 0f

    // Normalize Y values to fit within [height, 0] (inverted Y for canvas)
    var min = Float.POSITIVE_INFINITY
    var max = Float.NEGATIVE_INFINITY
    for (v in points) {
        if (v < min) min = v
        if (v > max) max = v
    }
    val span = max - min

    fun mapY(v: Float): Float {
        return if (span == 0f) {
            // Flat series: draw in the vertical center
            size.height / 2f
        } else {
            val norm = (v - min) / span
            size.height - norm * size.height
        }
    }

    // Start the path at the first point
    val y0 = mapY(points[0])
    moveTo(0f, y0)

    // Draw lines to subsequent points
    if (count > 1) {
        var x = stepX
        var i = 1
        while (i < count) {
            val y = mapY(points[i])
            lineTo(x, y)
            x += stepX
            i++
        }
    }
}

private val defaultStroke = Stroke(
    width = 1F,
    cap = StrokeCap.Round,
    join = StrokeJoin.Round,
)

@Composable
fun rememberPoints(
    points: FloatArray,
    stroke: Stroke = defaultStroke,
    pointColor: Color = MaterialTheme.colorScheme.secondary,
    lineColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    gridColor: Color = MaterialTheme.colorScheme.outlineVariant,
    labelColor: Color = MaterialTheme.colorScheme.outline,
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
    label: @Composable () -> Unit,
): Points = Points(
    points = points,
    stroke = stroke,
    pointColor = pointColor,
    lineColor = lineColor,
    gridColor = gridColor,
    label = {
        CompositionLocalProvider(
            LocalTextStyle provides labelStyle,
            LocalContentColor provides labelColor,
            content = label,
        )
    },
)
