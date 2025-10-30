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

package com.looker.kenko.ui.sessions

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

@HiltViewModel
class SessionsViewModel @Inject constructor(
    repo: SessionRepo,
) : ViewModel() {
    private val sessionsStream: Flow<List<Session>> = repo.stream

    private val isCurrentSessionActive: Flow<Boolean> = repo.streamByDate(localDate).map { it != null }

    val state: StateFlow<SessionsUiData> = combine(
        sessionsStream,
        isCurrentSessionActive,
    ) { sessions, isCurrentSessionActive ->
        SessionsUiData(
            sessions = sessions.filter { it.sets.isNotEmpty() },
            isCurrentSessionActive = isCurrentSessionActive,
        )
    }.asStateFlow(SessionsUiData(emptyList(), false))
}

@Stable
data class SessionsUiData(
    val sessions: List<Session>,
    val isCurrentSessionActive: Boolean,
)
