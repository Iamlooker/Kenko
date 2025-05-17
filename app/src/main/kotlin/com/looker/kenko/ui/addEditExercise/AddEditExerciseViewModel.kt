/*
 * Copyright (C) 2025. LooKeR & Contributors
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

package com.looker.kenko.ui.addEditExercise

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.looker.kenko.R
import com.looker.kenko.data.StringHandler
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.ui.addEditExercise.navigation.AddEditExerciseRoute
import com.looker.kenko.utils.asStateFlow
import com.looker.kenko.utils.isValidUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class AddEditExerciseViewModel @Inject constructor(
    private val repo: ExerciseRepo,
    private val stringHandler: StringHandler,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val routeData: AddEditExerciseRoute = savedStateHandle.toRoute()

    private val exerciseId: Int? = routeData.id

    private val defaultTarget: MuscleGroups? = routeData.target?.let { MuscleGroups.valueOf(it) }

    private val targetMuscle = MutableStateFlow(MuscleGroups.Chest)

    private val isIsometric = MutableStateFlow(false)

    private val isReadOnly: Boolean = exerciseId != null

    val snackbarState = SnackbarHostState()

    var exerciseName: String by mutableStateOf("")
        private set

    var reference: String by mutableStateOf("")
        private set

    private val isReferenceInvalid = snapshotFlow { reference }
        .debounce(200.milliseconds)
        .mapLatest { it.isValidUrl() && it.isNotBlank() }

    private val exerciseAlreadyExistError = snapshotFlow { exerciseName }
        .debounce(200.milliseconds)
        .mapLatest { repo.isExerciseAvailable(it) && !isReadOnly }

    val state = combine(
        targetMuscle,
        isIsometric,
        flowOf(isReadOnly),
        exerciseAlreadyExistError,
        isReferenceInvalid,
    ) { target, isometric, readOnly, alreadyExist, referenceInvalid ->
        AddEditExerciseUiState(
            targetMuscle = target,
            isIsometric = isometric,
            isReadOnly = readOnly,
            isError = alreadyExist,
            isReferenceInvalid = referenceInvalid,
        )
    }.asStateFlow(
        AddEditExerciseUiState(
            targetMuscle = MuscleGroups.Chest,
            isIsometric = false,
            isError = false,
            isReadOnly = false,
            isReferenceInvalid = false,
        ),
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

    fun addNewExercise(onDone: () -> Unit) {
        viewModelScope.launch {
            if (exerciseName.isBlank()) {
                snackbarState.showSnackbar(stringHandler.getString(R.string.error_exercise_name_empty))
                return@launch
            }
            if (state.value.isReferenceInvalid) {
                snackbarState.showSnackbar(stringHandler.getString(R.string.error_invalid_reference_format))
                return@launch
            }
            repo.upsert(
                Exercise(
                    name = exerciseName,
                    target = targetMuscle.value,
                    reference = reference.ifBlank { null },
                    isIsometric = isIsometric.value,
                    id = exerciseId,
                ),
            )
            onDone()
        }
    }

    init {
        viewModelScope.launch {
            if (exerciseId != null) {
                val exercise = repo.get(exerciseId)
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

@Stable
data class AddEditExerciseUiState(
    val targetMuscle: MuscleGroups,
    val isIsometric: Boolean,
    val isError: Boolean,
    val isReadOnly: Boolean,
    val isReferenceInvalid: Boolean,
)
