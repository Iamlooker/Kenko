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

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

@Immutable
class Points(
    val points: FloatArray, // Only y points since x-axis is always aligned to start
    val pointColor: Color,
    val lineColor: Color,
    val gridColor: Color,
    val smoothing: Float, // 0f = straight lines; 1f = maximum smoothing
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

    // Precompute mapped points with even X spacing
    val mappedY = FloatArray(count) { i -> mapY(points[i]) }
    val mappedX = FloatArray(count) { i -> if (count > 1) i * stepX else 0f }

    val s = smoothing.coerceIn(0f, 1f)

    // Start the path at the first point
    moveTo(mappedX[0], mappedY[0])

    if (count == 1) return@apply

    if (s == 0f || count < 3) {
        // Fallback to straight lines when no smoothing or not enough points
        var i = 1
        while (i < count) {
            lineTo(mappedX[i], mappedY[i])
            i++
        }
        return@apply
    }

    // Cubic smoothing using Catmull–Rom-like control points
    // For segment Pi -> P(i+1):
    //   c1 = Pi + (Pi+1 - P(i-1)) * (s / 6)
    //   c2 = P(i+1) - (P(i+2) - Pi) * (s / 6)
    // Endpoints use mirroring of neighbors.
    val tension = s / 6f

    var i = 0
    while (i < count - 1) {
        val i0 = (i - 1).coerceAtLeast(0)
        val i1 = i
        val i2 = i + 1
        val i3 = (i + 2).coerceAtMost(count - 1)

        val x0 = mappedX[i0]; val y0 = mappedY[i0]
        val x1 = mappedX[i1]; val y1 = mappedY[i1]
        val x2 = mappedX[i2]; val y2 = mappedY[i2]
        val x3 = mappedX[i3]; val y3 = mappedY[i3]

        val cx1 = x1 + (x2 - x0) * tension
        val cy1 = y1 + (y2 - y0) * tension
        val cx2 = x2 - (x3 - x1) * tension
        val cy2 = y2 - (y3 - y1) * tension

        cubicTo(cx1, cy1, cx2, cy2, x2, y2)
        i++
    }
}

@Composable
fun rememberPoints(
    points: FloatArray,
    pointColor: Color = MaterialTheme.colorScheme.secondary,
    lineColor: Color = MaterialTheme.colorScheme.primaryContainer,
    gridColor: Color = MaterialTheme.colorScheme.outlineVariant,
    smoothing: Float = 0.3F,
): Points = Points(
    points = points,
    pointColor = pointColor,
    lineColor = lineColor,
    gridColor = gridColor,
    smoothing = smoothing,
)
