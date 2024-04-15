package com.looker.kenko.ui.sessions

import androidx.lifecycle.ViewModel
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SessionsViewModel @Inject constructor(
    repo: SessionRepo,
) : ViewModel() {
    val sessionsStream = repo.stream.asStateFlow(emptyList())
}