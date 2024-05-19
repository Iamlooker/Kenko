package com.looker.kenko.ui.home

import androidx.lifecycle.ViewModel
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    planRepo: PlanRepo,
    sessionRepo: SessionRepo,
) : ViewModel() {

    private val planStream = planRepo.current

    private val sessionStream = sessionRepo.getStream(localDate)

    private val sessionsStream = sessionRepo.stream

    val state = combine(
        planStream,
        sessionStream,
        sessionsStream,
    ) { currentPlan, currentSession, sessions ->
        HomeUiData(
            isPlanSelected = currentPlan != null,
            isSessionStarted = currentSession != null,
            hasMultipleSessions = sessions.isNotEmpty(),
        )
    }.asStateFlow(
        HomeUiData(
            isPlanSelected = false,
            isSessionStarted = false,
            hasMultipleSessions = false,
        )
    )

}

data class HomeUiData(
    val isPlanSelected: Boolean,
    val isSessionStarted: Boolean,
    val hasMultipleSessions: Boolean,
)
