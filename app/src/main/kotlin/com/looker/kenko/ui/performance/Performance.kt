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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.data.model.Rating
import com.looker.kenko.utils.formatDate
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.FloatLinearAxisModel
import io.github.koalaplot.core.xygraph.IntLinearAxisModel
import io.github.koalaplot.core.xygraph.Point
import io.github.koalaplot.core.xygraph.XYGraph
import kotlinx.datetime.LocalDate
import kotlin.collections.component1
import kotlin.collections.component2

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun Performance(
    viewModel: PerformanceViewModel,
    onAddNewExercise: () -> Unit,
) {
    val selectedExercise by viewModel.selectedExercise.collectAsStateWithLifecycle()
    val exercises by viewModel.exercises.collectAsStateWithLifecycle()
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun Chart(
    rating: Map<LocalDate, Rating>,
    modifier: Modifier = Modifier,
) {
    ChartLayout(
        modifier = Modifier.size(
            width = 400.dp,
            height = 200.dp,
        ) then modifier,
    ) {
        val xRange = remember(rating) {
            val keys = rating.keys.map { it.toEpochDays() }
            if (keys.isEmpty()) return@remember 0..1
            keys.min()..keys.max()
        }
        val yRange = remember(rating) {
            val values = rating.values.map { it.value.toFloat() }
            if (values.isEmpty()) return@remember 0F..1F
            (values.min() * 0.99F)..(values.max() * 1.05F)
        }
        XYGraph(
            xAxisModel = IntLinearAxisModel(xRange),
            xAxisLabels = { formatDate(it) },
            yAxisModel = FloatLinearAxisModel(yRange),
        ) {
            val points = remember(rating) {
                rating.map { (key, value) ->
                    Point(
                        key.toEpochDays(),
                        value.value.toFloat(),
                    )
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
