package com.looker.kenko.ui.planEdit

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.looker.kenko.R
import com.looker.kenko.data.StringHandler
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.ui.planEdit.navigation.PlanEditRoute
import com.looker.kenko.utils.asStateFlow
import com.looker.kenko.utils.updateAsMutable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class PlanEditViewModel @Inject constructor(
    private val repo: PlanRepo,
    private val stringHandler: StringHandler,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val routeData: PlanEditRoute = savedStateHandle.toRoute<PlanEditRoute>()

    private val planId: Long? = routeData.id.takeIf { it != -1L }

    private val planStream: Flow<Plan?> = repo.get(planId)

    var planName: String by mutableStateOf("")
        private set

    val snackbarState = SnackbarHostState()

    private val _dayAndExercises =
        MutableStateFlow<Map<DayOfWeek, List<Exercise>>>(emptyMap())

    private val _dayOfWeek: MutableStateFlow<DayOfWeek> = MutableStateFlow(DayOfWeek.THURSDAY)

    private val _isSheetVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val state: StateFlow<PlanEditUiData> = combine(
        _dayOfWeek,
        _dayAndExercises,
        _isSheetVisible,
    ) { day, exerciseMap, sheetVisible ->
        val exercises = exerciseMap[day] ?: emptyList()
        PlanEditUiData(
            currentDay = day,
            isSheetVisible = sheetVisible,
            exercises = exercises,
        )
    }.asStateFlow(PlanEditUiData(DayOfWeek.THURSDAY, false, emptyList()))

    fun setName(value: String) {
        planName = value
    }

    fun setCurrentDay(dayOfWeek: DayOfWeek) {
        viewModelScope.launch {
            _dayOfWeek.emit(dayOfWeek)
        }
    }

    fun openSheet() {
        viewModelScope.launch {
            _isSheetVisible.emit(true)
        }
    }

    fun closeSheet() {
        viewModelScope.launch {
            _isSheetVisible.emit(false)
        }
    }

    fun addExercise(exercise: Exercise) {
        modifyExercise(state.value.currentDay) { add(exercise) }
    }

    fun removeExercise(exercise: Exercise) {
        modifyExercise(state.value.currentDay) { remove(exercise) }
    }

    fun savePlan(onDone: () -> Unit) {
        viewModelScope.launch {
            if (_dayAndExercises.value.isEmpty()) {
                snackbarState.showSnackbar(stringHandler.getString(R.string.error_plan_empty))
                return@launch
            }
            if (planName.isBlank()) {
                snackbarState.showSnackbar(stringHandler.getString(R.string.error_plan_name_empty))
                return@launch
            }
            repo.upsert(
                Plan(
                    exercisesPerDay = _dayAndExercises.value.toSortedMap(),
                    id = planId,
                    name = planName,
                    isActive = true,
                )
            )
            onDone()
        }
    }

    private fun modifyExercise(
        dayOfWeek: DayOfWeek,
        block: MutableList<Exercise>.() -> Unit,
    ) {
        viewModelScope.launch {
            _dayAndExercises.update {
                it.updateAsMutable {
                    val exerciseMap = getOrPut(dayOfWeek) { emptyList() }.updateAsMutable(block)
                    if (exerciseMap.isNotEmpty()) {
                        set(dayOfWeek, exerciseMap)
                    } else {
                        remove(dayOfWeek)
                    }
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            val plan = planStream.filterNotNull().firstOrNull() ?: return@launch
            setName(plan.name)
            plan.exercisesPerDay.forEach { (day, exercises) ->
                launch {
                    exercises.forEach { exercise ->
                        launch {
                            modifyExercise(day) {
                                add(exercise)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Stable
data class PlanEditUiData(
    val currentDay: DayOfWeek,
    val isSheetVisible: Boolean,
    val exercises: List<Exercise>,
)
