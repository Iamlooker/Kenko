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

package com.looker.kenko.ui.sessionDetail

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.looker.kenko.R
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.PlanItem
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.model.week
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.ui.sessionDetail.navigation.SessionDetailRoute
import com.looker.kenko.utils.asStateFlow
import com.looker.kenko.utils.isToday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import javax.inject.Inject

@HiltViewModel
class SessionDetailViewModel @Inject constructor(
    private val repo: SessionRepo,
    private val planRepo: PlanRepo,
    savedStateHandle: SavedStateHandle,
    private val uriHandler: UriHandler,
) : ViewModel() {

    private val routeData: SessionDetailRoute = savedStateHandle.toRoute<SessionDetailRoute>()

    private val epochDays: Int? = routeData.epochDays.takeIf { it != -1 }

    private val sessionDate: LocalDate = epochDays?.let {
        LocalDate.fromEpochDays(it)
    } ?: localDate

    val previousSessionDate = sessionDate - week

    private val previousSessionExists: Flow<Boolean> = repo.streamByDate(previousSessionDate)
        .map { it != null }

    private val sessionStream: Flow<Session?> = repo.streamByDate(sessionDate)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val exercisesToday: Flow<List<Exercise>> = sessionStream.flatMapLatest { session ->
        if (sessionDate.isToday) {
            planRepo.activeExercises(sessionDate.dayOfWeek)
        } else if (session != null) {
            if (session.planId != null) {
                planRepo.planItems(session.planId, sessionDate.dayOfWeek)
                    .map { it.map(PlanItem::exercise) }
            } else {
                flowOf(session.sets.map { it.exercise })
            }
        } else {
            flowOf(emptyList())
        }
    }

    private val _currentExercise: MutableStateFlow<Exercise?> = MutableStateFlow(null)
    val current: StateFlow<Exercise?> = _currentExercise

    val state: StateFlow<SessionDetailState> =
        combine(
            sessionStream,
            exercisesToday,
            previousSessionExists,
        ) { session, exercises, previousSession ->
            if (session == null && epochDays != null) {
                return@combine SessionDetailState.Error.InvalidSession
            }

            if (exercises.isEmpty() && sessionDate.isToday) {
                return@combine SessionDetailState.Error.EmptyPlan
            }

            val currentSession = session ?: Session(-1, emptyList())

            val exerciseMap = exercises
                .associateWith { exercise ->
                    currentSession.sets.filter { it.exercise.id == exercise.id }
                }

            SessionDetailState.Success(
                SessionUiData(
                    date = currentSession.date,
                    sets = exerciseMap,
                    isToday = currentSession.date.isToday,
                    hasPreviousSession = previousSession,
                ),
            )
        }.onStart { emit(SessionDetailState.Loading) }
            .asStateFlow(SessionDetailState.Loading)

    fun removeSet(setId: Int?) {
        if (setId == null) return
        viewModelScope.launch {
            repo.removeSet(setId)
        }
    }

    fun showBottomSheet(exercise: Exercise) {
        viewModelScope.launch {
            _currentExercise.emit(exercise)
        }
    }

    fun hideSheet() {
        viewModelScope.launch {
            _currentExercise.emit(null)
        }
    }

    fun openReference(reference: String) {
        viewModelScope.launch {
            try {
                uriHandler.openUri(reference)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }
}

@Stable
data class SessionUiData(
    val date: LocalDate,
    val sets: Map<Exercise, List<Set>>,
    val isToday: Boolean = false,
    val hasPreviousSession: Boolean = false,
)

sealed interface SessionDetailState {

    data object Loading : SessionDetailState

    data class Success(val data: SessionUiData) : SessionDetailState

    sealed class Error(
        @StringRes val title: Int,
        @StringRes val errorMessage: Int,
    ) : SessionDetailState {
        data object InvalidSession : Error(
            title = R.string.label_missed_day,
            errorMessage = R.string.error_cant_find_session,
        )

        data object EmptyPlan : Error(
            title = R.string.label_nothing_today,
            errorMessage = R.string.label_no_exercise_today,
        )
    }
}
