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

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.looker.kenko.data.repository.Performance
import com.looker.kenko.utils.formatDate
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.FloatLinearAxisModel
import io.github.koalaplot.core.xygraph.IntLinearAxisModel
import io.github.koalaplot.core.xygraph.Point
import io.github.koalaplot.core.xygraph.XYGraph

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun Performance(
    viewModel: PerformanceViewModel,
    onAddNewExercise: () -> Unit,
) {
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun PerformancePlot(
    performance: Performance,
    xRange: IntRange,
    yRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
) {
    ChartLayout(
        modifier = Modifier.size(
            width = 400.dp,
            height = 200.dp,
        ) then modifier,
    ) {
        XYGraph(
            xAxisModel = IntLinearAxisModel(xRange),
            xAxisLabels = { formatDate(it) },
            yAxisModel = FloatLinearAxisModel(yRange),
        ) {
            val points = remember(performance) {
                performance.days.mapIndexed { index, day ->
                    Point(x = day, y = performance.ratings[index].toFloat())
                }
            }
            val lineColor = MaterialTheme.colorScheme.tertiary
            LinePlot(
                data = points,
                lineStyle = LineStyle(SolidColor(lineColor), 2.dp),
            )
        }
    }
}
