package com.looker.kenko.ui.sessions

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SessionsViewModel @Inject constructor(
    repo: SessionRepo,
) : ViewModel() {
    private val sessionsStream: Flow<List<Session>> = repo.stream.map { it.asReversed() }

    private val isCurrentSessionActive: Flow<Boolean> = repo.getStream(localDate).map { it != null }

    val state: StateFlow<SessionsUiData> = combine(
        sessionsStream,
        isCurrentSessionActive,
    ) { sessions, isCurrentSessionActive ->
        SessionsUiData(
            sessions = sessions,
            isCurrentSessionActive = isCurrentSessionActive,
        )
    }.asStateFlow(SessionsUiData(emptyList(), false))
}

@Stable
data class SessionsUiData(
    val sessions: List<Session>,
    val isCurrentSessionActive: Boolean,
)
