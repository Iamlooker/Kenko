package com.looker.kenko.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.looker.kenko.ui.KenkoAppState
import com.looker.kenko.ui.addEditExercise.navigation.addEditExercise
import com.looker.kenko.ui.addEditExercise.navigation.navigateToAddEditExercise
import com.looker.kenko.ui.exercises.navigation.exercises
import com.looker.kenko.ui.exercises.navigation.navigateToExercises
import com.looker.kenko.ui.getStarted.navigation.GET_STARTED_ROUTE
import com.looker.kenko.ui.getStarted.navigation.getStarted
import com.looker.kenko.ui.performance.navigation.performance
import com.looker.kenko.ui.planEdit.navigation.navigateToPlanEdit
import com.looker.kenko.ui.planEdit.navigation.planEdit
import com.looker.kenko.ui.plans.navigation.navigateToPlans
import com.looker.kenko.ui.plans.navigation.plans
import com.looker.kenko.ui.profile.navigation.navigateToProfile
import com.looker.kenko.ui.profile.navigation.profile
import com.looker.kenko.ui.sessionDetail.navigation.navigateToSessionDetail
import com.looker.kenko.ui.sessionDetail.navigation.sessionDetail
import com.looker.kenko.ui.sessions.navigation.navigateToSessions
import com.looker.kenko.ui.sessions.navigation.sessions

private val singleTopNavOptions = navOptions {
    launchSingleTop = true
}

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
        getStarted { isOnboardingDone ->
            if (isOnboardingDone) {
                navController.navigateToSessions(navOptions = singleTopNavOptions)
            } else {
                navController.navigateToProfile(navOptions = singleTopNavOptions)
            }
        }

        sessions {
            navController.navigateToSessionDetail(
                date = it,
                navOptions = singleTopNavOptions
            )
        }

        plans {
            navController.navigateToPlanEdit(
                id = it,
                navOptions = singleTopNavOptions
            )
        }

        profile(
            onNavigateToAddExercise = {
                navController.navigateToAddEditExercise(navOptions = singleTopNavOptions)
            },
            onNavigateToExercisesList = {
                navController.navigateToExercises(navOptions = singleTopNavOptions)
            },
            onNavigateToPlans = {
                navController.navigateToPlans(navOptions = singleTopNavOptions)
            },
        )

        exercises { exerciseName, target ->
            navController.navigateToAddEditExercise(
                exerciseName = exerciseName,
                target = target,
                navOptions = singleTopNavOptions
            )
        }

        planEdit(navController::popBackStack) {
            navController.navigateToAddEditExercise(navOptions = singleTopNavOptions)
        }

        sessionDetail(navController::popBackStack)

        addEditExercise(navController::popBackStack)

        performance()

    }
}
