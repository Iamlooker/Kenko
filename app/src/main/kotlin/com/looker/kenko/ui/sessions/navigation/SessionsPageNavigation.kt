package com.looker.kenko.ui.sessions.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.ui.sessions.Sessions
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
object SessionRoute

fun NavController.navigateToSessions(navOptions: NavOptions? = null) {
    navigate(SessionRoute, navOptions = navOptions)
}

fun NavGraphBuilder.sessions(
    onSessionClick: (LocalDate?) -> Unit,
    onBackPress: () -> Unit,
) {
    composable<SessionRoute> {
        Sessions(
            onSessionClick = onSessionClick,
            onBackPress = onBackPress,
            viewModel = hiltViewModel(),
        )
    }
}
