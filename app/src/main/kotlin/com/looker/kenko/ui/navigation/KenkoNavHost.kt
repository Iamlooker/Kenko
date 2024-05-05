package com.looker.kenko.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.looker.kenko.ui.KenkoAppState
import com.looker.kenko.ui.addEditExercise.navigation.addEditExercise
import com.looker.kenko.ui.addEditExercise.navigation.navigateToAddEditExercise
import com.looker.kenko.ui.getStarted.navigation.GET_STARTED_ROUTE
import com.looker.kenko.ui.getStarted.navigation.getStarted
import com.looker.kenko.ui.planEdit.navigation.navigateToPlanEdit
import com.looker.kenko.ui.planEdit.navigation.planEdit
import com.looker.kenko.ui.plans.navigation.navigateToPlans
import com.looker.kenko.ui.plans.navigation.plans
import com.looker.kenko.ui.profile.navigation.profile
import com.looker.kenko.ui.sessionDetail.navigation.navigateToSessionDetail
import com.looker.kenko.ui.sessionDetail.navigation.sessionDetail
import com.looker.kenko.ui.sessions.navigation.navigateToSessions
import com.looker.kenko.ui.sessions.navigation.sessions

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
        getStarted(navController::navigateToSessions)

        sessions(navController::navigateToSessionDetail)

        plans(navController::navigateToPlanEdit)

        profile(
            onNavigateToAddExercise = navController::navigateToAddEditExercise,
            onNavigateToPlans = navController::navigateToPlans,
            onNavigateToExercisesList = navController::navigateToAddEditExercise,
        )

        planEdit(navController::popBackStack, navController::navigateToAddEditExercise)

        sessionDetail(navController::popBackStack)

        addEditExercise(navController::popBackStack)

    }
}
