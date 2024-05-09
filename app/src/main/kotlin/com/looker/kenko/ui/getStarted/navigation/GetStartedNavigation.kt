package com.looker.kenko.ui.getStarted.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.looker.kenko.ui.getStarted.GetStarted

const val GET_STARTED_ROUTE = "get_started"

// We never navigate "TO" Get Started Screen

fun NavGraphBuilder.getStarted(onNext: (Boolean) -> Unit) {
    composable(
        route = GET_STARTED_ROUTE,
    ) {
        GetStarted(onNext)
    }
}
