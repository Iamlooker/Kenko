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

package com.looker.kenko.ui.performance

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.ui.performance.components.Plot
import com.looker.kenko.ui.performance.components.drawAxes
import com.looker.kenko.ui.performance.components.drawGrid
import com.looker.kenko.ui.performance.components.mapXY
import com.looker.kenko.ui.performance.components.pathFor
import com.looker.kenko.ui.performance.components.rememberPlot
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun Performance(viewModel: PerformanceViewModel) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    Surface(Modifier.safeContentPadding()) {
        when (uiState) {
            is PerformanceUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is PerformanceUiState.Success -> {
                val data = (uiState as PerformanceUiState.Success).data
                PerformancePlot(
                    plot = rememberPlot(data.performance.ratings, data.performance.days),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                )
            }

            is PerformanceStateError -> {
                val message = when (uiState) {
                    PerformanceStateError.NoValidPerformance -> "No performance"
                    PerformanceStateError.NotEnoughData -> "Not enough data"
                    else -> error("Unknown")
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = message, color = MaterialTheme.colorScheme.tertiary)
                }
            }
        }
    }
}

@Composable
private fun PerformancePlot(
    plot: Plot,
    modifier: Modifier = Modifier,
) {
    val axesColor = MaterialTheme.colorScheme.outline
    val gridColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.3F)
    Canvas(modifier) {
        val points = plot.mapXY(size)
        drawGrid(gridColor)
        drawAxes(axesColor)

        drawPath(
            path = pathFor(points),
            color = plot.lineColor,
            style = plot.style,
        )

        points.forEach {
            drawCircle(
                center = it,
                color = plot.pointColor,
                radius = plot.pointRadius,
            )
        }
    }
}

@Preview
@Composable
private fun PerformanceSmallPreview() {
    val ratings = floatArrayOf(8852.628F, 5092.6157F, 7432.0F)
    val days = intArrayOf(0, 1, 9)
    KenkoTheme {
        PerformancePlot(
            rememberPlot(ratings, days),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(8.dp),
        )
    }
}

@Preview
@Composable
private fun PerformancePreview() {
    val ratings = floatArrayOf(
        8852.628F,
        3092.6155F,
        2661.0654F,
        11071.365F,
        5737.648F,
        4238.7886F,
        11071.365F
    )
    val days = intArrayOf(0, 1, 2, 3, 11, 15, 20)
    KenkoTheme {
        PerformancePlot(
            rememberPlot(ratings, days),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(8.dp),
        )
    }
}
