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

package com.looker.kenko.ui.performance

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.looker.kenko.R
import com.looker.kenko.data.repository.Performance
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.numbers
import kotlin.math.ceil
import kotlin.math.floor

private val graphSizeModifier = Modifier.size(
    width = 400.dp,
    height = 250.dp,
)

@Composable
fun Performance(
    viewModel: PerformanceViewModel,
    onAddNewExercise: () -> Unit,
) {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.label_coming_soon),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}

@Composable
private fun PerformancePlot(
    performance: Performance,
    modifier: Modifier = Modifier,
    yStep: Float = 1.0f,
    yPadding: Float = 0.5f,
    xStep: Float = 1.0f,
    xPadding: Float = 0.0f,
    contentPadding: Dp = 40.dp,
    pointColor: Color = MaterialTheme.colorScheme.primary,
    pathColor: Color = MaterialTheme.colorScheme.primary.copy(0.6F),
    gridColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
    axisLabelColor: Color = MaterialTheme.colorScheme.onSurface,
    showPoints: Boolean = true,
    showPath: Boolean = true,
    showHorizontalGrid: Boolean = true,
    showVerticalGrid: Boolean = true,
    gridPathEffect: PathEffect = GridLineEffect,
    gridStrokeWidth: Dp = 1.dp,
    pathStrokeWidth: Dp = 3.dp,
    pointRadius: Dp = 4.dp,
    yLabelFormatter: (Float) -> String = { "%.1f".format(it) },
    xLabelFormatter: (Float) -> String = { "%.0f".format(it) },
    yAxisMin: Float? = null,
    yAxisMax: Float? = null,
    xAxisMin: Float? = null,
    xAxisMax: Float? = null,
) {
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.bodyMedium.numbers()

    Canvas(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.large,
            ),
    ) {
        val internalPadding = contentPadding.toPx()

        val graphAreaWidth = size.width - (2 * internalPadding)
        val graphAreaHeight = size.height - (2 * internalPadding)

        val yStepSafe = if (yStep > 0f) yStep else 1f
        val (axisYMin, axisYMax) = computeAxis(
            rawMin = performance.ratings.min(),
            rawMax = performance.ratings.max(),
            step = yStepSafe,
            padding = yPadding,
            axisMinOverride = yAxisMin,
            axisMaxOverride = yAxisMax,
        )
        val axisYRange = axisYMax - axisYMin

        val xStepSafe = if (xStep > 0f) xStep else 1f
        val (axisXMin, axisXMax) = computeAxis(
            rawMin = performance.days.min().toFloat(),
            rawMax = performance.days.max().toFloat(),
            step = xStepSafe,
            padding = xPadding,
            axisMinOverride = xAxisMin,
            axisMaxOverride = xAxisMax,
        )
        val axisXRange = (axisXMax - axisXMin)

        drawGridLines(
            internalPadding = internalPadding,
            graphAreaWidth = graphAreaWidth,
            graphAreaHeight = graphAreaHeight,
            axisMin = axisYMin,
            axisMax = axisYMax,
            yStep = yStepSafe,
            axisXMin = axisXMin,
            axisXMax = axisXMax,
            xStep = xStepSafe,
            gridColor = gridColor,
            axisLabelColor = axisLabelColor,
            textMeasurer = textMeasurer,
            textStyle = textStyle,
            showHorizontalGrid = showHorizontalGrid,
            showVerticalGrid = showVerticalGrid,
            gridPathEffect = gridPathEffect,
            gridStrokeWidth = gridStrokeWidth,
            yLabelFormatter = yLabelFormatter,
            xLabelFormatter = xLabelFormatter,
        )

        val path = Path()

        performance.ratings.forEachIndexed { index, rating ->
            val day = performance.days.getOrNull(index) ?: 0
            val xPercentage = (day - axisXMin) / axisXRange
            val x = internalPadding + (xPercentage * graphAreaWidth)

            val yPercentage = (rating - axisYMin) / axisYRange
            val y = internalPadding + graphAreaHeight - (yPercentage * graphAreaHeight)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        if (showPath) {
            drawPath(
                path = path,
                color = pathColor,
                style = Stroke(width = pathStrokeWidth.toPx()),
            )
        }

        if (showPoints) {
            performance.ratings.forEachIndexed { index, rating ->
                val day = performance.days.getOrNull(index) ?: 0
                val xPercentage = (day - axisXMin) / axisXRange
                val x = internalPadding + (xPercentage * graphAreaWidth)

                val yPercentage = (rating - axisYMin) / axisYRange
                val y = internalPadding + graphAreaHeight - (yPercentage * graphAreaHeight)

                drawCircle(
                    color = pointColor,
                    radius = pointRadius.toPx(),
                    center = Offset(x, y),
                )
            }
        }
    }
}

private fun DrawScope.drawGridLines(
    internalPadding: Float,
    graphAreaWidth: Float,
    graphAreaHeight: Float,
    axisMin: Float,
    axisMax: Float,
    yStep: Float,
    axisXMin: Float,
    axisXMax: Float,
    xStep: Float,
    gridColor: Color,
    axisLabelColor: Color,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    showHorizontalGrid: Boolean,
    showVerticalGrid: Boolean,
    gridPathEffect: PathEffect,
    gridStrokeWidth: Dp,
    yLabelFormatter: (Float) -> String,
    xLabelFormatter: (Float) -> String,
) {
    val totalSteps = kotlin.math.round(((axisMax - axisMin) / yStep).toDouble()).toInt()
    val axisRange = axisMax - axisMin
    if (showHorizontalGrid) {
        for (i in 0..totalSteps) {
            val value = axisMin + i * yStep
            val yPercentage = if (axisRange == 0f) 0.5f else (value - axisMin) / axisRange
            val y = internalPadding + graphAreaHeight - (yPercentage * graphAreaHeight)
            drawLine(
                color = gridColor,
                start = Offset(internalPadding, y),
                end = Offset(internalPadding + graphAreaWidth, y),
                strokeWidth = gridStrokeWidth.toPx(),
                pathEffect = gridPathEffect,
            )
            val textLayoutResult = textMeasurer.measure(
                text = yLabelFormatter(value),
                style = textStyle,
            )
            drawText(
                textLayoutResult = textLayoutResult,
                color = axisLabelColor,
                topLeft = Offset(
                    internalPadding - textLayoutResult.size.width - 8.dp.toPx(),
                    y - textLayoutResult.size.height / 2,
                ),
            )
        }
    }

    // Vertical grid and X-axis labels using xStep and axisXMin/axisXMax
    val xTotalSteps = kotlin.math.round(((axisXMax - axisXMin).toDouble() / xStep)).toInt()
    val xRange = (axisXMax - axisXMin).toFloat()
    for (i in 0..xTotalSteps) {
        val xValue = axisXMin + i * xStep
        val xPercentage = if (xRange == 0f) 0.5f else (xValue - axisXMin).toFloat() / xRange
        val x = internalPadding + (xPercentage * graphAreaWidth)
        if (showVerticalGrid) {
            drawLine(
                color = gridColor,
                start = Offset(x, internalPadding),
                end = Offset(x, internalPadding + graphAreaHeight),
                strokeWidth = gridStrokeWidth.toPx(),
                pathEffect = gridPathEffect,
            )
        }
        val dayLabel = xLabelFormatter(xValue)
        val textLayoutResult = textMeasurer.measure(text = dayLabel, style = textStyle)
        drawText(
            textLayoutResult = textLayoutResult,
            color = axisLabelColor,
            topLeft = Offset(
                x - textLayoutResult.size.width / 2,
                internalPadding + graphAreaHeight + 4.dp.toPx(),
            ),
        )
    }
}

private val GridLineEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

private fun computeAxis(
    rawMin: Float,
    rawMax: Float,
    step: Float,
    padding: Float,
    axisMinOverride: Float?,
    axisMaxOverride: Float?,
): Pair<Float, Float> {
    val computedMin = floor(((rawMin - padding) / step).toDouble()).toFloat() * step
    val computedMax = ceil(((rawMax + padding) / step).toDouble()).toFloat() * step
    var axisMin = axisMinOverride ?: computedMin
    var axisMax = axisMaxOverride ?: computedMax
    if (axisMinOverride != null && axisMaxOverride == null) {
        axisMax = maxOf(axisMin + step, computedMax)
    } else if (axisMaxOverride != null && axisMinOverride == null) {
        axisMin = minOf(axisMax - step, computedMin)
    }
    if (axisMax == axisMin) {
        axisMax = axisMin + step
    }
    return axisMin to axisMax
}

@Preview(showBackground = false)
@Composable
private fun PerformancePlotPreview() {
    KenkoTheme {
        PerformancePlot(
            modifier = graphSizeModifier,
            performance = Performance(
                days = intArrayOf(1, 2, 3, 4, 5, 6),
                ratings = floatArrayOf(1.5F, 2F, 4F, 2.5F, 3F, 6F),
            ),
            yStep = 1.0f,
            yPadding = 0.5f,
            xStep = 0f,
            xPadding = 0f,
        )
    }
}
