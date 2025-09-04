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

import androidx.annotation.FloatRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.util.fastCoerceAtLeast
import kotlin.math.sign

@Immutable
class Plot(
    val ratings: FloatArray,
    val days: IntArray,
    val pointColor: Color,
    val pointRadius: Float,
    val lineColor: Color,
    val style: Stroke,
    @param:FloatRange(from = 0.0, to = 1.0, toInclusive = false)
    val smoothing: Float,
)

fun pathFor(
    points: Array<Point>,
    @FloatRange(from = 0.0, to = 1.0, toInclusive = false)
    smoothing: Float,
) = Path().apply {
    val pointCount = points.size
    if (pointCount == 0) return@apply

    moveTo(points[0])

    if (smoothing == 0f || pointCount < 3) {
        // Fallback to straight lines when no smoothing or not enough points
        points.forEach { lineTo(it) }
    } else {
        // Monotone cubic Hermite interpolation (Fritsch–Carlson) to avoid overshoot
        // Compute slopes for each interval and tangents per point
        val slopes = FloatArray(pointCount - 1) { i ->
            val current = points[i]
            val next = points[i + 1]
            // Segment width: use actual deltaX between adjacent mapped X coordinates to keep slope scale consistent
            val dy = next.y - current.y
            val dx = next.x - current.x
            if (dx == 0F) Float.POSITIVE_INFINITY * sign(dy)
            else dy / dx
        }
        val tangents = FloatArray(pointCount)
        // Endpoint tangents
        tangents[0] = slopes[0]
        tangents[pointCount - 1] = slopes[pointCount - 2]
        // Internal tangents with Fritsch–Carlson formula
        for (i in 1..<pointCount - 1) {
            val slopePrev = slopes[i - 1]
            val slopeNext = slopes[i]
            val isInverting = sign(slopePrev) != sign(slopeNext)
            if (slopePrev == 0f || slopeNext == 0f || isInverting) {
                tangents[i] = 0f
            } else {
                // Equal spacing simplifies weights to 3 / (1/slopePrev + 1/slopeNext)
                tangents[i] = (3F * slopePrev * slopeNext) / (slopePrev + slopeNext)
            }
        }
        // Apply smoothing factor (0..1)
        for (i in tangents.indices) {
            tangents[i] *= smoothing
        }

        // Convert Hermite to cubic Bézier per segment and draw
        for (i in 0..<pointCount - 1) {
            val (x1, y1) = points[i]
            val (x2, y2) = points[i + 1]
            val deltaX = x2 - x1
            val c1x = x1 + deltaX / 3f
            val c1y = y1 + (tangents[i] * deltaX) / 3f
            val c2x = x2 - deltaX / 3f
            val c2y = y2 - (tangents[i + 1] * deltaX) / 3f
            cubicTo(x1 = c1x, y1 = c1y, x2 = c2x, y2 = c2y, x3 = x2, y3 = y2)
        }
    }
}

private const val HorizontalInset = 0.04F
private const val VerticalInset = 0.06F

fun Plot.mapXY(
    size: Size,
    horizontalPaddingPercentage: Float = HorizontalInset,
    verticalPaddingPercentage: Float = VerticalInset,
): Array<Point> {
    val pointCount = ratings.size

    val horizontalInset = size.width * horizontalPaddingPercentage
    val verticalInset = size.height * verticalPaddingPercentage

    val usableWidth = (size.width - horizontalInset - horizontalInset).fastCoerceAtLeast(0f)
    val usableHeight = (size.height - verticalInset - verticalInset).fastCoerceAtLeast(0f)

    var minRating = Float.POSITIVE_INFINITY
    var maxRating = Float.NEGATIVE_INFINITY
    for (i in 0..<pointCount) {
        val rating = ratings[i]
        if (rating < minRating) minRating = rating
        if (rating > maxRating) maxRating = rating
    }

    val minDay = days.first()
    val maxDay = days.last()

    return Array(pointCount) { i ->
        val rating = ratings[i]
        val normalized = (rating - minRating) / (maxRating - minRating)
        val y = verticalInset + (1f - normalized) * usableHeight

        val day = days[i]
        val normalizedDay = (day - minDay).toFloat() / (maxDay - minDay)
        val x = horizontalInset + normalizedDay * usableWidth
        Point(x, y)
    }
}

@Composable
fun rememberPlot(
    points: FloatArray,
    days: IntArray,
    pointColor: Color = MaterialTheme.colorScheme.primary,
    pointRadius: Float = 8F,
    lineColor: Color = MaterialTheme.colorScheme.primaryContainer,
    style: Stroke = PlotStyle,
    smoothing: Float = 0.4F,
): Plot = Plot(
    ratings = points,
    days = days,
    pointColor = pointColor,
    pointRadius = pointRadius,
    lineColor = lineColor,
    style = style,
    smoothing = smoothing,
)

private val PlotStyle = Stroke(
    width = 8F,
    cap = StrokeCap.Round,
    join = StrokeJoin.Round,
    miter = 0F
)
