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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import javax.inject.Inject

@HiltViewModel
class SessionDetailViewModel @Inject constructor(
    private val repo: SessionRepo,
    planRepo: PlanRepo,
    savedStateHandle: SavedStateHandle,
    private val uriHandler: UriHandler,
) : ViewModel() {

    private val routeData: SessionDetailRoute = savedStateHandle.toRoute<SessionDetailRoute>()

    private val epochDays: Int? = routeData.epochDays.takeIf { it != -1 }

    private val sessionDate: LocalDate = epochDays?.let {
        LocalDate.fromEpochDays(it)
    } ?: localDate

    val previousSessionDate = sessionDate - week

    private val previousSessionExists: Flow<Boolean> = repo.getStream(previousSessionDate)
            .map { it != null }

    private val sessionStream: Flow<Session?> = repo.getStream(sessionDate)

    private val exercisesStream: Flow<List<Exercise>?> = planRepo.exercises(sessionDate)

    private val _currentExercise: MutableStateFlow<Exercise?> = MutableStateFlow(null)
    val current: StateFlow<Exercise?> = _currentExercise

    val state: StateFlow<SessionDetailState> =
        combine(
            sessionStream,
            exercisesStream,
            previousSessionExists,
        ) { session, exercises, previousSession ->
            if (session == null && epochDays != null) {
                return@combine SessionDetailState.Error.InvalidSession
            }

            val currentSession = session ?: Session.create(emptyList())

            if (exercises == null && session == null) {
                return@combine SessionDetailState.Error.EmptyPlan
            }

            val setsExerciseMap = exercises?.associateWith { exercise ->
                currentSession.sets.filter { exercise == it.exercise }
            } ?: currentSession.sets.groupBy { it.exercise }

            SessionDetailState.Success(
                SessionUiData(
                    session = currentSession,
                    sets = setsExerciseMap,
                    isToday = currentSession.date.isToday,
                    hasPreviousSession = previousSession
                )
            )
        }.onStart { emit(SessionDetailState.Loading) }
            .asStateFlow(SessionDetailState.Loading)

    fun removeSet(set: Set) {
        viewModelScope.launch {
            repo.removeSet(set)
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
                // do nothing
            }
        }
    }
}

@Stable
data class SessionUiData(
    val session: Session,
    val sets: Map<Exercise, List<Set>>?,
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
