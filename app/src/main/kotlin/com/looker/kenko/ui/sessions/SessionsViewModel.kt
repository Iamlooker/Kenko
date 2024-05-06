package com.looker.kenko.ui.sessions

import androidx.lifecycle.ViewModel
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SessionsViewModel @Inject constructor(
    repo: SessionRepo,
) : ViewModel() {
    val sessionsStream = repo.stream.map { it.asReversed() }.asStateFlow(emptyList())

    val isCurrentSessionActive: StateFlow<Boolean> =
        repo.getStream(localDate)
            .map { it != null }
            .asStateFlow(false)

}