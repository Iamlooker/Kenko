package com.looker.kenko.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.looker.kenko.ui.KenkoAppState
import com.looker.kenko.ui.addEditExercise.navigation.addEditExercise
import com.looker.kenko.ui.addEditExercise.navigation.navigateToAddEditExercise
import com.looker.kenko.ui.getStarted.navigation.GET_STARTED_ROUTE
import com.looker.kenko.ui.getStarted.navigation.getStarted
import com.looker.kenko.ui.planEdit.navigation.navigateToPlanEdit
import com.looker.kenko.ui.planEdit.navigation.planEdit
import com.looker.kenko.ui.plans.navigation.plans
import com.looker.kenko.ui.profile.navigation.profile
import com.looker.kenko.ui.sessionDetail.navigation.navigateToSessionDetail
import com.looker.kenko.ui.sessionDetail.navigation.sessionDetail
import com.looker.kenko.ui.sessions.navigation.SESSIONS_ROUTE
import com.looker.kenko.ui.sessions.navigation.sessions
import kotlinx.datetime.LocalDate

@Composable
fun KenkoNavHost(
    appState: KenkoAppState,
    modifier: Modifier = Modifier,
    startDestination: String = GET_STARTED_ROUTE,
) {
    val navController = appState.navController
    NavHost(
        modifier = modifier,
        navController = navController as NavHostController,
        startDestination = startDestination,
    ) {
        getStarted(navController::navigateToTopLevel)

        topLevelDestination(
            onSessionClick = navController::navigateToSessionDetail,
            onNavigateToAddEditExercise = navController::navigateToAddEditExercise,
            onNavigateToPlanEdit = navController::navigateToPlanEdit
        )

        planEdit(navController::popBackStack, navController::navigateToAddEditExercise)

        sessionDetail(navController::popBackStack)

        addEditExercise(navController::popBackStack)

    }
}

private fun NavController.navigateToTopLevel() {
    navigate("topLevel")
}

fun NavGraphBuilder.topLevelDestination(
    onSessionClick: (LocalDate?) -> Unit,
    onNavigateToAddEditExercise: () -> Unit,
    onNavigateToPlanEdit: (Long?) -> Unit,
) {
    navigation(SESSIONS_ROUTE, "topLevel") {
        sessions(onSessionClick)
        plans(onNavigateToPlanEdit)
        profile(onNavigateToAddEditExercise, onNavigateToAddEditExercise)
    }
}
