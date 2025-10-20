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

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.repository.Performance
import com.looker.kenko.data.repository.PerformanceRepo
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

@HiltViewModel
class PerformanceViewModel @Inject constructor(
    private val repo: PerformanceRepo,
    planRepo: PlanRepo,
) : ViewModel() {

    val currentPlan = planRepo.current

    val state: StateFlow<PerformanceUiState> = currentPlan
        .map { plan ->
            if (plan == null) {
                PerformanceStateError.NoValidPerformance
            } else {
                val performance = repo.getPerformance(planId = plan.id)
                when {
                    performance == null -> PerformanceStateError.NoValidPerformance
                    performance.ratings.size < MinDataRequired -> PerformanceStateError.NotEnoughData
                    else -> PerformanceUiState.Success(
                        PerformanceUiData(
                            plan = plan,
                            performance = performance,
                        ),
                    )
                }
            }
        }
        .onStart { emit(PerformanceUiState.Loading) }
        .asStateFlow(PerformanceUiState.Loading)

}

private const val MinDataRequired = 1

sealed interface PerformanceUiState {
    object Loading : PerformanceUiState
    class Success(val data: PerformanceUiData) : PerformanceUiState
}

sealed interface PerformanceStateError : PerformanceUiState {
    object NoValidPerformance : PerformanceStateError
    object NotEnoughData : PerformanceStateError
}

@Stable
class PerformanceUiData(
    val plan: Plan?,
    val performance: Performance,
)
