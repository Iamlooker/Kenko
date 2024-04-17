package com.looker.kenko.ui.addEditExercise

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.ui.addEditExercise.navigation.ARG_EXERCISE_NAME
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class AddEditExerciseViewModel @Inject constructor(
    private val repo: ExerciseRepo,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val defaultExerciseName: String? = savedStateHandle[ARG_EXERCISE_NAME]

    private val _targetMuscle = MutableStateFlow(MuscleGroups.Chest)
    val targetMuscle: StateFlow<MuscleGroups> = _targetMuscle.asStateFlow()

    private val _isIsometric = MutableStateFlow(false)
    val isIsometric: StateFlow<Boolean> = _isIsometric.asStateFlow()

    val isReadOnly: Boolean = defaultExerciseName != null

    var exerciseName: String by mutableStateOf(defaultExerciseName ?: "")
        private set

    var reference: String by mutableStateOf("")
        private set

    val exerciseAlreadyExistError: StateFlow<Boolean> =
        snapshotFlow { exerciseName }
            .mapLatest { repo.isExerciseAvailable(it) && !isReadOnly }
            .asStateFlow(false)

    fun setName(value: String) {
        exerciseName = value
    }

    fun addReference(value: String) {
        reference = value
    }

    fun setTargetMuscle(value: MuscleGroups) {
        viewModelScope.launch {
            _targetMuscle.emit(value)
        }
    }

    fun setIsometric(value: Boolean) {
        viewModelScope.launch {
            _isIsometric.emit(value)
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
                    Log.e("tag", it.toString())
                    setName(it.name)
                    addReference(it.reference ?: "")
                    setIsometric(it.isIsometric)
                    setTargetMuscle(it.target)
                }
            }
        }
    }
}