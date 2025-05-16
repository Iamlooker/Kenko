package com.looker.kenko.ui.getStarted

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.looker.kenko.ui.getStarted.navigation.GetStartedRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GetStartedOldViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val routeData: GetStartedRoute = savedStateHandle.toRoute()
    val isOnboardingDone = routeData.isOnboardingDone

}
