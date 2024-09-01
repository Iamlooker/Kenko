package com.looker.kenko.ui.getStarted.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.looker.kenko.ui.getStarted.GetStarted
import kotlinx.serialization.Serializable

@Serializable
object GetStartedRoute

fun NavGraphBuilder.getStarted(onNext: () -> Unit) {
    composable<GetStartedRoute> {
        GetStarted(onNext)
    }
}
