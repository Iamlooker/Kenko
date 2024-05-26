package com.looker.kenko.ui.exercises

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.R
import com.looker.kenko.data.StringHandler
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val repo: ExerciseRepo,
    private val uriHandler: UriHandler,
    private val stringHandler: StringHandler,
) : ViewModel() {

    // null -> all
    private val selectedTarget: MutableStateFlow<MuscleGroups?> = MutableStateFlow(null)

    private val exercisesStream: Flow<List<Exercise>> = repo.stream

    val snackbarState = SnackbarHostState()

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

    fun removeExercise(name: String) {
        viewModelScope.launch {
            repo.remove(name)
        }
    }

    fun setTarget(value: MuscleGroups?) {
        viewModelScope.launch {
            selectedTarget.emit(value)
        }
    }

    fun onReferenceClick(reference: String) {
        viewModelScope.launch {
            try {
                uriHandler.openUri(reference)
            } catch (e: IllegalStateException) {
                snackbarState.showSnackbar(
                    e.message ?: stringHandler.getString(R.string.error_invalid_url)
                )
            }
        }
    }
}

val MuscleGroups?.string: Int
    @StringRes
    get() = this?.stringRes ?: R.string.label_all_muscle_groups

@Stable
class ExercisesUiState(
    val exercises: List<Exercise> = emptyList(),
    val selected: MuscleGroups? = null,
)
