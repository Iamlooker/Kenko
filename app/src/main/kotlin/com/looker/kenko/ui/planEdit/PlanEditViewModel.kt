package com.looker.kenko.ui.planEdit

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.utils.asStateFlow
import com.looker.kenko.utils.updateAsMutable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class PlanEditViewModel @Inject constructor(
    private val repo: PlanRepo,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val planId: Long? = savedStateHandle["id"]

    private val planStream: Flow<Plan?> = repo.get(planId)

    private val planName: TextFieldState = TextFieldState()
    private val _isActive: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _dayAndExercises: MutableStateFlow<Map<DayOfWeek, List<Exercise>>> =
        MutableStateFlow(emptyMap())

    val currentPlan: StateFlow<Plan> = combine(
        _isActive,
        _dayAndExercises
    ) { isActive, dayAndExercise ->
        Plan(
            name = planName.text.toString(),
            exercisesPerDay = dayAndExercise,
            isActive = isActive
        )
    }.asStateFlow(Plan("", emptyMap(), false))

    fun addDay(dayOfWeek: DayOfWeek) {
        viewModelScope.launch {
            _dayAndExercises.update {
                it.updateAsMutable {
                    put(dayOfWeek, emptyList())
                }
            }
        }
    }

    fun setActive(value: Boolean) {
        viewModelScope.launch {
            _isActive.emit(value)
        }
    }

    fun addExercise(dayOfWeek: DayOfWeek, exercise: Exercise) {
        viewModelScope.launch {
            _dayAndExercises.update {
                val newList = it[dayOfWeek]
                    ?.updateAsMutable { add(exercise) }
                    ?: listOf(exercise)
                it.updateAsMutable {
                    set(dayOfWeek, newList)
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            val plan = planStream.filterNotNull().first()
            setActive(plan.isActive)
            plan.exercisesPerDay.forEach { (day, exercises) ->
                launch {
                    exercises.forEach { exercise ->
                        launch {
                            addExercise(day, exercise)
                        }
                    }
                }
            }
        }
    }
}
