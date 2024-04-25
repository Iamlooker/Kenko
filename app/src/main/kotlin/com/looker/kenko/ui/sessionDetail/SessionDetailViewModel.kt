package com.looker.kenko.ui.sessionDetail

import androidx.annotation.StringRes
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.R
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.ui.sessionDetail.navigation.ARG_SESSION_ID
import com.looker.kenko.utils.asStateFlow
import com.looker.kenko.utils.isToday
import com.looker.kenko.utils.updateAsMutable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class SessionDetailViewModel @Inject constructor(
    private val repo: SessionRepo,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val epochDays: Int? = savedStateHandle
        .get<Int?>(ARG_SESSION_ID)
        .takeIf { it != -1 }

    private val sessionDate: LocalDate = epochDays?.let {
        LocalDate.fromEpochDays(it)
    } ?: localDate

    private val sessionStream: Flow<Session?> = repo.getStream(sessionDate)

    private val exercisesStream: Flow<List<Exercise>?> = repo.exercisesToday

    var currentExercise: Exercise? by mutableStateOf(null)
        private set

    val isSheetExpanded: Boolean by derivedStateOf {
        currentExercise != null
    }

    val state: StateFlow<SessionDetailState> =
        combine(
            sessionStream,
            exercisesStream,
        ) { session, exercises ->
            if (session == null && epochDays != null) {
                return@combine SessionDetailState.Error.InvalidSession
            }

            if (exercises == null) {
                return@combine SessionDetailState.Error.EmptyPlan
            }

            val currentSession = session ?: Session.create(emptyList())

            val setsExerciseMap = exercises.associateWith { exercise ->
                currentSession.sets.filter { exercise == it.exercise }
            }
            SessionDetailState.Success(
                SessionUiData(
                    session = currentSession,
                    sets = setsExerciseMap,
                    isToday = currentSession.date.isToday
                )
            )
        }.onStart { emit(SessionDetailState.Loading) }
            .asStateFlow(SessionDetailState.Loading)


    fun showBottomSheet(exercise: Exercise) {
        currentExercise = exercise
    }

    fun addSet(set: Set) {
        viewModelScope.launch {
            if (sessionDate.isToday) {
                if (sessionStream.first() == null) {
                    repo.createEmpty()
                }
                val currentSession = requireNotNull(sessionStream.first())
                repo.updateSet(currentSession.sets.updateAsMutable { add(set) })
            }
        }
    }

    fun hideSheet() {
        currentExercise = null
    }
}

data class SessionUiData(
    val session: Session,
    val sets: Map<Exercise, List<Set>>?,
    val isToday: Boolean = false,
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
            errorMessage = R.string.label_cant_find_session,
        )

        data object EmptyPlan : Error(
            title = R.string.label_nothing_today,
            errorMessage = R.string.label_no_exercise_today,
        )
    }
}
