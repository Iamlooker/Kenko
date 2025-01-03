package com.looker.kenko.ui.planEdit

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.looker.kenko.R
import com.looker.kenko.data.StringHandler
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.PlanItem
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.ui.planEdit.navigation.PlanEditRoute
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
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

    private val planId: Int? = routeData.id.takeIf { it != -1 }

    // if null show name edit else plan edit
    private val _planId = MutableStateFlow(planId)

    val planNameState: TextFieldState = TextFieldState("")

    val snackbarState = SnackbarHostState()

    private val _isBackAlreadyPressedOnce = MutableStateFlow(false)

    private val _dayOfWeek: MutableStateFlow<DayOfWeek> = MutableStateFlow(localDate.dayOfWeek)

    private val _isSheetVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _fullDaySelection: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val pageState: StateFlow<PlanEditStage> = _planId.map { id ->
        if (id == null) PlanEditStage.NameEdit else PlanEditStage.PlanEdit
    }.asStateFlow(PlanEditStage.NameEdit)

    val state: StateFlow<PlanEditState> = combine(
        _planId.filterNotNull(),
        _dayOfWeek,
        _fullDaySelection,
        _isSheetVisible,
    ) { id, day, daySelection, sheetVisible ->
        PlanEditState(
            currentDay = day,
            selectionMode = daySelection,
            exerciseSheetVisible = sheetVisible,
            exercises = repo.getPlanItems(id, day).map(PlanItem::exercise),
        )
    }.asStateFlow(
        PlanEditState(
            currentDay = localDate.dayOfWeek,
            selectionMode = false,
            exerciseSheetVisible = false,
            exercises = emptyList(),
        ),
    )

    val isNameAlreadyUsed = snapshotFlow { planNameState }.map {
        repo.planNameExists(it.text.toString())
    }.asStateFlow(false)

    fun saveName() {
        viewModelScope.launch {
            if (planNameState.text.isNotBlank()) {
                val createId = repo.createPlan(planNameState.text.toString())
                _planId.emit(createId)
            } else {
                snackbarState.showSnackbar(stringHandler.getString(R.string.error_plan_name_empty))
            }
        }
    }

    fun setCurrentDay(dayOfWeek: DayOfWeek) {
        viewModelScope.launch {
            _dayOfWeek.emit(dayOfWeek)
            if (_fullDaySelection.value) {
                _fullDaySelection.emit(false)
            }
        }
    }

    fun openFullDaySelection() {
        viewModelScope.launch {
            _fullDaySelection.emit(true)
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
        viewModelScope.launch {
            _planId.value?.let { id ->
                repo.addItem(
                    PlanItem(
                        dayOfWeek = _dayOfWeek.value,
                        exercise = exercise,
                        planId = id,
                    ),
                )
            }
        }
    }

    fun removeExercise(exercise: Exercise) {
        viewModelScope.launch {
            repo.removeItemById(exercise.id!!)
        }
    }

    fun onBackPress(stage: PlanEditStage, onBackPress: () -> Unit) {
        viewModelScope.launch {
            if (stage == PlanEditStage.NameEdit) {
                onBackPress()
                return@launch
            }
            if (_isBackAlreadyPressedOnce.value) {
                repo.deletePlan(_planId.value!!)
                onBackPress()
                return@launch
            }
            if (repo.getPlanItems(_planId.value!!).isEmpty()) {
                _isBackAlreadyPressedOnce.emit(true)
                snackbarState.showSnackbar(stringHandler.getString(R.string.error_plan_empty_prompt))
                return@launch
            }
            onBackPress()
        }
    }
}

@Stable
enum class PlanEditStage {
    NameEdit,
    PlanEdit,
}

@Stable
data class PlanEditState(
    val currentDay: DayOfWeek,
    val selectionMode: Boolean,
    val exerciseSheetVisible: Boolean,
    val exercises: List<Exercise>,
)
