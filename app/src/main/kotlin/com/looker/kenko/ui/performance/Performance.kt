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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.repository.Performance
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.numbers

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
) {
    val pointColor = MaterialTheme.colorScheme.primary
    val pathColor = MaterialTheme.colorScheme.primary.copy(0.6F)
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
    val axisLabelColor = MaterialTheme.colorScheme.onSurface
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.bodyMedium.numbers()

    Canvas(
        modifier = modifier
            .then(graphSizeModifier)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.large,
            ),
    ) {
        val internalPadding = 40.dp.toPx()

        val graphAreaWidth = size.width - (2 * internalPadding)
        val graphAreaHeight = size.height - (2 * internalPadding)

        val yMax = performance.ratings.maxOrNull() ?: 0f
        val yMin = performance.ratings.minOrNull() ?: 0f
        val yRange =
            if (yMax == yMin) 1f else yMax - yMin
        val verticalGridLines = if (performance.days.size > 1) performance.days.size - 1 else 1

        drawGridLines(
            internalPadding = internalPadding,
            graphAreaWidth = graphAreaWidth,
            graphAreaHeight = graphAreaHeight,
            verticalGridLines = verticalGridLines,
            yMax = yMax,
            yRange = yRange,
            performance = performance,
            gridColor = gridColor ,
            axisLabelColor = axisLabelColor ,
            textMeasurer = textMeasurer ,
            textStyle = textStyle,
        )

        val path = Path()
        val xPointStep =
            if (performance.ratings.size > 1) graphAreaWidth / (performance.ratings.size - 1)
            else graphAreaWidth / 2

        performance.ratings.forEachIndexed { index, rating ->
            val x = internalPadding + index * xPointStep
            val yPercentage = if (yRange == 0f) 0.5f else (rating - yMin) / yRange
            val y = internalPadding + graphAreaHeight - (yPercentage * graphAreaHeight)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = pathColor,
            style = Stroke(width = 3.dp.toPx()),
        )

        performance.ratings.forEachIndexed { index, rating ->
            val x = internalPadding + index * xPointStep
            val yPercentage = if (yRange == 0f) 0.5f else (rating - yMin) / yRange
            val y = internalPadding + graphAreaHeight - (yPercentage * graphAreaHeight)

            drawCircle(
                color = pointColor,
                radius = 4.dp.toPx(),
                center = Offset(x, y),
            )
        }
    }
}

private fun DrawScope.drawGridLines(
    internalPadding: Float,
    graphAreaWidth: Float,
    graphAreaHeight: Float,
    verticalGridLines: Int,
    yMax: Float,
    yRange: Float,
    performance: Performance,
    gridColor: Color,
    axisLabelColor: Color,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
) {
    for (i in 0..HorizontalLines) {
        val y = internalPadding + i * (graphAreaHeight / HorizontalLines)
        drawLine(
            color = gridColor,
            start = Offset(internalPadding, y),
            end = Offset(internalPadding + graphAreaWidth, y),
            strokeWidth = 1.dp.toPx(),
            pathEffect = GridLineEffect,
        )
        val labelValue = yMax - i * (yRange / HorizontalLines)
        val textLayoutResult = textMeasurer.measure(
            text = "%.1f".format(labelValue),
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

    val xStep = if (verticalGridLines > 0) graphAreaWidth / verticalGridLines else graphAreaWidth

    for (i in 0..verticalGridLines) {
        val x = internalPadding + i * xStep
        drawLine(
            color = gridColor,
            start = Offset(x, internalPadding),
            end = Offset(x, internalPadding + graphAreaHeight),
            strokeWidth = 1.dp.toPx(),
            pathEffect = GridLineEffect,
        )
        if (i < performance.days.size) {
            val dayLabel = performance.days[i].toString()
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
}

private val GridLineEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
private const val HorizontalLines = 5

@Preview(showBackground = false)
@Composable
private fun PerformancePlotPreview() {
    KenkoTheme {
        PerformancePlot(
            performance = Performance(
                days = intArrayOf(1, 2, 3, 4, 5, 6),
                ratings = floatArrayOf(1.5F, 2F, 4F, 2.5F, 3F, 6F),
            ),
        )
    }
}
