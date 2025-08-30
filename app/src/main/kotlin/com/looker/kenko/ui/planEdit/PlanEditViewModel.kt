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
import com.looker.kenko.data.local.model.SetType
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.PlanItem
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.ui.planEdit.navigation.PlanEditRoute
import com.looker.kenko.utils.asStateFlow
import com.looker.kenko.utils.nextLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.incrementAndFetch
import kotlin.random.Random
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek

@HiltViewModel
class PlanEditViewModel @Inject constructor(
    private val repo: PlanRepo,
    private val stringHandler: StringHandler,
    private val sessionRepo: com.looker.kenko.data.repository.SessionRepo,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val routeData: PlanEditRoute = savedStateHandle.toRoute()

    private val _planId: Int = routeData.id

    // if null show name edit else plan edit
    private val planIdStream = MutableStateFlow(_planId)

    val planNameState: TextFieldState = TextFieldState("")

    val snackbarState = SnackbarHostState()

    private val _isBackAlreadyPressedOnce = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _planItemsStream = planIdStream.flatMapLatest { repo.planItems(it) }

    private val _dayOfWeek: MutableStateFlow<DayOfWeek> = MutableStateFlow(localDate.dayOfWeek)

    private val _isSheetVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _fullDaySelection: MutableStateFlow<Boolean> = MutableStateFlow(false)

    @OptIn(FlowPreview::class)
    val isNameAlreadyUsed = snapshotFlow { planNameState.text.trim().toString() }
        .debounce(200.milliseconds)
        .map { repo.planNameExists(it) }
        .asStateFlow(false)

    val pageState: StateFlow<PlanEditStage> = planIdStream.map { id ->
        if (id == -1) PlanEditStage.NameEdit else PlanEditStage.PlanEdit
    }.asStateFlow(PlanEditStage.NameEdit)

    val state: StateFlow<PlanEditState> = combine(
        _planItemsStream,
        _dayOfWeek,
        _fullDaySelection,
        _isSheetVisible,
    ) { items, day, daySelection, sheetVisible ->
        PlanEditState(
            currentDay = day,
            selectionMode = daySelection,
            exerciseSheetVisible = sheetVisible,
            exercises = items.filter { it.dayOfWeek == day }.map(PlanItem::exercise),
        )
    }.asStateFlow(
        PlanEditState(
            currentDay = localDate.dayOfWeek,
            selectionMode = false,
            exerciseSheetVisible = false,
            exercises = emptyList(),
        ),
    )

    fun saveName() {
        viewModelScope.launch {
            if (planNameState.text.isBlank()) {
                snackbarState.showSnackbar(stringHandler.getString(R.string.error_plan_name_empty))
                return@launch
            }
            if (isNameAlreadyUsed.value) {
                snackbarState.showSnackbar(stringHandler.getString(R.string.error_plan_name_exists))
                return@launch
            }
            val createId = repo.createPlan(planNameState.text.toString())
            planIdStream.emit(createId)
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
            repo.addItem(
                PlanItem(
                    dayOfWeek = _dayOfWeek.value,
                    exercise = exercise,
                    planId = planIdStream.value,
                ),
            )
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
                repo.deletePlan(planIdStream.value)
                onBackPress()
                return@launch
            }
            if (repo.getPlanItems(planIdStream.value).isEmpty()) {
                _isBackAlreadyPressedOnce.emit(true)
                snackbarState.showSnackbar(stringHandler.getString(R.string.error_plan_empty_prompt))
                return@launch
            }
            onBackPress()
        }
    }

    @OptIn(ExperimentalAtomicApi::class)
    fun debugFillMockData(sessions: Int = 4) {
        viewModelScope.launch {
            val now = Clock.System.now()
            val rand = Random.Default
            val added = AtomicInt(0)
            val planId = planIdStream.value
            repeat(sessions) {
                val date = rand.nextLocalDateTime(
                    from = now - (sessions * 2).days,
                    until = now,
                ).date

                val sessionId = sessionRepo.getSessionIdOrCreate(date)
                val items = repo.getPlanItems(planId, date.dayOfWeek)

                for (item in items) {
                    val exerciseId = item.exercise.id ?: continue
                    launch {
                        val setsCount = rand.nextInt(1, 4)
                        repeat(setsCount) {
                            val weight = rand.nextInt(10, 80) + rand.nextFloat()
                            val reps = rand.nextInt(5, 15)
                            sessionRepo.addSet(
                                sessionId = sessionId,
                                exerciseId = exerciseId,
                                weight = weight,
                                reps = reps,
                                setType = SetType.entries.random(),
                            )
                            added.incrementAndFetch()
                        }
                    }
                }
            }
            snackbarState.showSnackbar("Mock data added: ${added.load()} sets")
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
