package com.looker.kenko.ui.addEditExercise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.ui.addEditExercise.navigation.AddEditExerciseRoute
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class AddEditExerciseViewModel @Inject constructor(
    private val repo: ExerciseRepo,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val routeData = savedStateHandle.toRoute<AddEditExerciseRoute>()

    private val defaultExerciseName: String? = routeData.exerciseName

    private val defaultTarget: MuscleGroups? = routeData.target?.let { MuscleGroups.valueOf(it) }

    private val targetMuscle = MutableStateFlow(MuscleGroups.Chest)

    private val isIsometric = MutableStateFlow(false)

    private val isReadOnly: Boolean = defaultExerciseName != null

    var exerciseName: String by mutableStateOf(defaultExerciseName ?: "")
        private set

    var reference: String by mutableStateOf("")
        private set

    private val exerciseAlreadyExistError: Flow<Boolean> =
        snapshotFlow { exerciseName }
            .mapLatest { repo.isExerciseAvailable(it) && !isReadOnly }

    val state = combine(
        targetMuscle,
        isIsometric,
        flowOf(isReadOnly),
        exerciseAlreadyExistError
    ) { target, isometric, readOnly, alreadyExist ->
        AddEditExerciseUiState(
            targetMuscle = target,
            isIsometric = isometric,
            isReadOnly = readOnly,
            isError = alreadyExist
        )
    }.asStateFlow(
        AddEditExerciseUiState(
            targetMuscle = MuscleGroups.Chest,
            isIsometric = false,
            isError = false,
            isReadOnly = false,
        )
    )

    fun setName(value: String) {
        exerciseName = value
    }

    fun addReference(value: String) {
        reference = value
    }

    fun setTargetMuscle(value: MuscleGroups) {
        viewModelScope.launch {
            targetMuscle.emit(value)
        }
    }

    fun setIsometric(value: Boolean) {
        viewModelScope.launch {
            isIsometric.emit(value)
        }
    }

    fun addNewExercise() {
        viewModelScope.launch {
            repo.upsert(
                Exercise(
                    name = exerciseName,
                    target = targetMuscle.value,
                    reference = reference,
                    isIsometric = isIsometric.value
                )
            )
        }
    }

    init {
        viewModelScope.launch {
            if (defaultExerciseName != null) {
                val exercise = repo.get(defaultExerciseName)
                exercise?.let {
                    setName(it.name)
                    addReference(it.reference ?: "")
                    setIsometric(it.isIsometric)
                    setTargetMuscle(it.target)
                }
            } else if (defaultTarget != null) {
                setTargetMuscle(defaultTarget)
            }
        }
    }
}

data class AddEditExerciseUiState(
    val targetMuscle: MuscleGroups,
    val isIsometric: Boolean,
    val isError: Boolean,
    val isReadOnly: Boolean,
)

