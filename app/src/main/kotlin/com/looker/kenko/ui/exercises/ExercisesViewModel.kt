package com.looker.kenko.ui.exercises

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.R
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    repo: ExerciseRepo,
) : ViewModel() {

    val targets: List<MuscleGroups?> = listOf(null) + MuscleGroups.entries

    // null -> all
    private val _selectedTarget: MutableStateFlow<MuscleGroups?> = MutableStateFlow(null)
    val selectedTarget: StateFlow<MuscleGroups?> = _selectedTarget.asStateFlow()

    private val exercisesStream: Flow<List<Exercise>> = repo.stream

    val exercises: StateFlow<ExercisesUiState> = combine(
        exercisesStream,
        selectedTarget,
    ) { exercises, target ->
        val selectedExercises = if (target == null) {
            exercises
        } else {
            exercises.filter { it.target == target }
        }
        ExercisesUiState(
            exercises = selectedExercises,
            selected = target,
        )
    }.asStateFlow(ExercisesUiState())

    fun setTarget(value: MuscleGroups?) {
        viewModelScope.launch {
            _selectedTarget.emit(value)
        }
    }
}

val MuscleGroups?.string: Int
    @StringRes
    get() = this?.stringRes ?: R.string.all

@Stable
class ExercisesUiState(
    val exercises: List<Exercise> = emptyList(),
    val selected: MuscleGroups? = null,
)
