package com.looker.kenko.ui.sessions.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.ui.sessions.Sessions
import kotlinx.datetime.LocalDate

const val SESSIONS_ROUTE = "sessions"

fun NavController.navigateToSessions(navOptions: NavOptions? = null) {
    navigate(SESSIONS_ROUTE, navOptions = navOptions)
}

fun NavGraphBuilder.sessions(onSessionClick: (LocalDate?) -> Unit) {
    composable(
        route = SESSIONS_ROUTE
    ) {
        Sessions(onSessionClick = onSessionClick, viewModel = hiltViewModel())
    }
}