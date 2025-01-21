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
import androidx.lifecycle.viewModelScope
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.model.PlanItem
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.data.repository.Performance
import com.looker.kenko.data.repository.PerformanceRepo
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.max
import kotlin.collections.min

@HiltViewModel
class PerformanceViewModel @Inject constructor(
    private val repo: PerformanceRepo,
    private val exerciseRepo: ExerciseRepo,
    private val planRepo: PlanRepo,
) : ViewModel() {

    private val plansAndExercises = hashMapOf<Plan, List<Exercise>>()

    private val _selectedPlan = MutableStateFlow<Int?>(null)
    private val _selectedExercise = MutableStateFlow<Int?>(null)
    val selectedExercise = _selectedExercise

    val exercises = exerciseRepo.stream.asStateFlow(emptyList())

    val state: StateFlow<PerformanceUiState> = combine(
        _selectedPlan,
        _selectedExercise,
    ) { plan, exercise ->
        val performance = repo.getPerformance(exercise, plan)
        if (performance == null) {
            return@combine PerformanceStateError.NoValidPerformance
        }
        if (performance.days.size <= 1 || performance.ratings.size <= 1) {
            return@combine PerformanceStateError.NotEnoughData
        }
        val days = performance.days
        val ratings = performance.ratings
        PerformanceUiState.Success(
            PerformanceUiData(
                exercise = exercise?.let { exerciseRepo.get(it) },
                plan = plan?.let { planRepo.plan(it) },
                performance = performance,
                xRange = days.min()..days.max(),
                yRange = (ratings.min().toFloat() * 0.99F)..(ratings.max().toFloat() * 1.05F),
            ),
        )
    }.onStart { emit(PerformanceUiState.Loading) }
        .asStateFlow(PerformanceUiState.Loading)

    fun selectExercise(id: Int) {
        viewModelScope.launch {
            _selectedExercise.emit(id)
        }
    }

    init {
        viewModelScope.launch {
            val plans = planRepo.plans.first()
            val planItemsMap = plans.associateWith {
                planRepo.getPlanItems(it.id!!).map(PlanItem::exercise)
            }
            plansAndExercises.putAll(planItemsMap)
        }
    }
}

sealed interface PerformanceUiState {
    data object Loading : PerformanceUiState

    data class Success(val data: PerformanceUiData) : PerformanceUiState
}

sealed interface PerformanceStateError : PerformanceUiState {
    data object NoValidPerformance : PerformanceStateError
    data object NotEnoughData : PerformanceStateError
}

@Stable
data class PerformanceUiData(
    val exercise: Exercise?,
    val plan: Plan?,
    val performance: Performance,
    val xRange: IntRange,
    val yRange: ClosedFloatingPointRange<Float>,
)
