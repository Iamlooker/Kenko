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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.data.repository.PerformanceRepo
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerformanceViewModel @Inject constructor(
    private val repo: PerformanceRepo,
    private val exerciseRepo: ExerciseRepo,
    private val planRepo: PlanRepo,
) : ViewModel() {

    private val plansStream = planRepo.plans
    private val exercisesStream = exerciseRepo.stream

    private val _selectedPlan = MutableStateFlow<Int?>(null)
    private val _selectedExercise = MutableStateFlow<Int?>(null)
    val selectedExercise = _selectedExercise

    val exercises = exerciseRepo.stream.asStateFlow(emptyList())

    fun selectExercise(id: Int) {
        viewModelScope.launch {
            _selectedExercise.emit(id)
        }
    }

    init {
        viewModelScope.launch {
            val exercise = exerciseRepo.stream.first().first()
            _selectedExercise.emit(exercise.id)
        }
    }
}
