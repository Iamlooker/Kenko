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
import kotlin.math.sign

@Immutable
class Points(
    val points: FloatArray, // Only y points since x-axis is always aligned to start
    val pointColor: Color,
    val lineColor: Color,
    val gridColor: Color,
    val smoothing: Float, // 0f = straight lines; 1f = maximum smoothing
)

fun Points.toPath(size: Size): Path = Path().apply {
    val pointCount = points.size
    if (pointCount == 0) return@apply

    // Internal padding (as percentage of canvas size) to avoid touching edges
    val horizontalInset = size.width * 0.04f
    val verticalInset = size.height * 0.06f

    val usableWidth = (size.width - horizontalInset - horizontalInset).coerceAtLeast(0f)
    val usableHeight = (size.height - verticalInset - verticalInset).coerceAtLeast(0f)

    // Determine horizontal spacing within padded area
    val xStep = if (pointCount > 1) usableWidth / (pointCount - 1) else 0f

    // Normalize Y values to fit within padded [topInset .. topInset+usableHeight] (inverted)
    var minYValue = Float.POSITIVE_INFINITY
    var maxYValue = Float.NEGATIVE_INFINITY
    for (value in points) {
        if (value < minYValue) minYValue = value
        if (value > maxYValue) maxYValue = value
    }
    val valueSpan = maxYValue - minYValue

    fun mapValueToCanvasY(value: Float): Float {
        return if (valueSpan == 0f) {
            // Flat series: draw in the vertical center of padded area
            verticalInset + usableHeight / 2f
        } else {
            val normalized = (value - minYValue) / valueSpan
            // Invert Y: 0 at bottom, 1 at top
            verticalInset + (1f - normalized) * usableHeight
        }
    }

    // Precompute mapped points with even X spacing inside padded area
    val mappedYCoordinates = FloatArray(pointCount) { i -> mapValueToCanvasY(points[i]) }
    val mappedXCoordinates = FloatArray(pointCount) { i ->
        if (pointCount > 1) horizontalInset + i * xStep else horizontalInset + usableWidth / 2f
    }

    val smoothingFactor = smoothing.coerceIn(0f, 1f)

    // Start the path at the first point
    moveTo(mappedXCoordinates[0], mappedYCoordinates[0])

    if (pointCount == 1) return@apply

    if (smoothingFactor == 0f || pointCount < 3) {
        // Fallback to straight lines when no smoothing or not enough points
        var i = 1
        while (i < pointCount) {
            lineTo(mappedXCoordinates[i], mappedYCoordinates[i])
            i++
        }
        return@apply
    }

    // Monotone cubic Hermite interpolation (Fritsch–Carlson) to avoid overshoot
    // Compute slopes for each interval and tangents per point
    val totalPoints = pointCount
    val segmentWidth = xStep.takeIf { it > 0f } ?: 1f
    val slopes =
        FloatArray(totalPoints - 1) { i -> (mappedYCoordinates[i + 1] - mappedYCoordinates[i]) / segmentWidth }
    val tangents = FloatArray(totalPoints)
    // Endpoint tangents
    tangents[0] = slopes[0]
    tangents[totalPoints - 1] = slopes[totalPoints - 2]
    // Internal tangents with Fritsch–Carlson formula
    for (i in 1..<totalPoints - 1) {
        val slopePrev = slopes[i - 1]
        val slopeNext = slopes[i]
        if (slopePrev == 0f || slopeNext == 0f || sign(slopePrev) != sign(slopeNext)) {
            tangents[i] = 0f
        } else {
            // Equal spacing simplifies weights to 3 / (1/slopePrev + 1/slopeNext)
            tangents[i] = (3f * slopePrev * slopeNext) / (slopePrev + slopeNext)
        }
    }
    // Apply smoothing factor (0..1)
    if (smoothingFactor != 1f) {
        var i = 0
        while (i < totalPoints) {
            tangents[i] *= smoothingFactor
            i++
        }
    }

    // Convert Hermite to cubic Bézier per segment and draw
    for (i in 0..<totalPoints - 1) {
        val x1 = mappedXCoordinates[i]
        val y1 = mappedYCoordinates[i]
        val x2 = mappedXCoordinates[i + 1]
        val y2 = mappedYCoordinates[i + 1]
        val deltaX = x2 - x1
        val c1x = x1 + deltaX / 3f
        var c1y = y1 + (tangents[i] * deltaX) / 3f
        val c2x = x2 - deltaX / 3f
        var c2y = y2 - (tangents[i + 1] * deltaX) / 3f
        // Clamp control Y within padded viewport to avoid shooting outside
        val minY = verticalInset
        val maxY = verticalInset + usableHeight
        if (c1y < minY) c1y = minY else if (c1y > maxY) c1y = maxY
        if (c2y < minY) c2y = minY else if (c2y > maxY) c2y = maxY
        cubicTo(c1x, c1y, c2x, c2y, x2, y2)
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
