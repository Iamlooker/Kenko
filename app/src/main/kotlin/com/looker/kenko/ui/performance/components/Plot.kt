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

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.util.fastCoerceAtLeast
import com.looker.kenko.utils.moveTo
import kotlin.math.sign

@Immutable
class Plot(
    val ratings: FloatArray,
    val days: IntArray,
    val pointColor: Color,
    val pointRadius: Float,
    val lineColor: Color,
    val style: Stroke,
)

fun pathFor(points: Array<Offset>) = Path().apply {
    if (points.isEmpty()) return@apply

    val slope = FloatArray(points.size)

    moveTo(points[0])
    // knowingly ignore the last index so the last tangent is horizontal
    for (i in 0..<points.lastIndex) {
        val (y, x) = points[i + 1] + points[i]
        slope[i] = if (x == 0F) Float.POSITIVE_INFINITY * sign(y)
        else (y / x) * Smoother
    }

    for (i in 0..<points.lastIndex) {
        val (x1, y1) = points[i]
        val (x2, y2) = points[i + 1]
        val deltaX = x2 - x1
        cubicTo(
            x1 = x1 + deltaX / 3f,
            y1 = y1 + (slope[i] * deltaX) / 3f,
            x2 = x2 - deltaX / 3f,
            y2 = y2 - (slope[i + 1] * deltaX) / 3f,
            x3 = x2, y3 = y2,
        )
    }
}

private const val Smoother = 0.3F

private const val HorizontalInset = 0.04F
private const val VerticalInset = 0.06F

fun Plot.mapXY(
    size: Size,
    horizontalPaddingPercentage: Float = HorizontalInset,
    verticalPaddingPercentage: Float = VerticalInset,
): Array<Offset> {
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
        Offset(x, y)
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
): Plot = Plot(
    ratings = points,
    days = days,
    pointColor = pointColor,
    pointRadius = pointRadius,
    lineColor = lineColor,
    style = style,
)

private val PlotStyle = Stroke(
    width = 8F,
    cap = StrokeCap.Round,
    join = StrokeJoin.Round,
    miter = 0F
)
