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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.data.repository.Performance
import com.looker.kenko.ui.theme.KenkoTheme

private val graphSizeModifier = Modifier.size(
    width = 400.dp,
    height = 200.dp,
)

@Composable
fun Performance(
    viewModel: PerformanceViewModel,
    onAddNewExercise: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    when (state) {
        PerformanceStateError.NoValidPerformance -> {

        }

        PerformanceStateError.NotEnoughData -> {

        }

        PerformanceUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is PerformanceUiState.Success -> {
            val data = (state as PerformanceUiState.Success).data
            PerformancePlot(data.performance)
        }
    }
}

@Composable
private fun PerformancePlot(
    performance: Performance,
    modifier: Modifier = Modifier,
) {
}

@Preview(showBackground = true)
@Composable
private fun PerformancePlotPreview() {
    KenkoTheme {
        PerformancePlot(
            performance = Performance(
                days = intArrayOf(1, 2, 3, 4, 5),
                ratings = floatArrayOf(1.5F, 2F, 4F, 2.5F, 3F),
            ),
        )
    }
}
