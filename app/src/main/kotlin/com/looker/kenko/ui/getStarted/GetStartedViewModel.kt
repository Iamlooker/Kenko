package com.looker.kenko.ui.getStarted

import androidx.lifecycle.ViewModel
import com.looker.kenko.data.repository.SettingsRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class GetStartedViewModel @Inject constructor(
    repo: SettingsRepo,
) : ViewModel() {

    val isOnboardingDone = repo.stream
        .map { it.isOnboardingDone }
        .asStateFlow(true)

}