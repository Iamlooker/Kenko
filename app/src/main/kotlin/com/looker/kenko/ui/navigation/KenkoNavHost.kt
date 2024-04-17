package com.looker.kenko.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.looker.kenko.ui.addEditExercise.navigation.addEditExercise
import com.looker.kenko.ui.getStarted.navigation.GET_STARTED_ROUTE
import com.looker.kenko.ui.getStarted.navigation.getStarted
import com.looker.kenko.ui.plans.navigation.navigateToPlans
import com.looker.kenko.ui.plans.navigation.plans
import com.looker.kenko.ui.sessionDetail.navigation.navigateToSessionDetail
import com.looker.kenko.ui.sessionDetail.navigation.sessionDetail
import com.looker.kenko.ui.sessions.navigation.navigateToSessions
import com.looker.kenko.ui.sessions.navigation.sessions

@Composable
fun KenkoNavHost(
    modifier: Modifier = Modifier,
    startDestination: String = GET_STARTED_ROUTE,
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        getStarted(navController::navigateToSessions)

        sessions(navController::navigateToSessionDetail)
        sessionDetail(navController::popBackStack)

        addEditExercise(navController::popBackStack)

        plans {  }
    }
}