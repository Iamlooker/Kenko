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
import com.looker.kenko.ui.getStarted.navigation.GetStartedRoute
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
import com.looker.kenko.ui.settings.navigation.navigateToSettings
import com.looker.kenko.ui.settings.navigation.settings

private val singleTopNavOptions = navOptions {
    launchSingleTop = true
}

private val splashNavOptions = navOptions {
    launchSingleTop = true
    popUpTo<GetStartedRoute> {
        inclusive = true
    }
}

@Composable
fun KenkoNavHost(
    appState: KenkoAppState,
    modifier: Modifier = Modifier,
    startDestination: Any = GetStartedRoute,
) {
    val navController = appState.navController
    NavHost(
        modifier = modifier,
        navController = navController as NavHostController,
        startDestination = startDestination,
    ) {
        getStarted { isOnboardingDone ->
            if (isOnboardingDone) {
                navController.navigateToSessions(navOptions = splashNavOptions)
            } else {
                navController.navigateToProfile(navOptions = splashNavOptions)
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

        settings(navController::popBackStack)

        profile(
            onAddExerciseClick = {
                navController.navigateToAddEditExercise(navOptions = singleTopNavOptions)
            },
            onExercisesClick = {
                navController.navigateToExercises(navOptions = singleTopNavOptions)
            },
            onPlanClick = {
                navController.navigateToPlans(navOptions = singleTopNavOptions)
            },
            onSettingsClick = {
                navController.navigateToSettings(navOptions = singleTopNavOptions)
            },
        )

        exercises(
            onExerciseClick = { exerciseName ->
                navController.navigateToAddEditExercise(
                    exerciseName = exerciseName,
                    target = null,
                    navOptions = singleTopNavOptions
                )
            },
            onCreateClick = { target ->
                navController.navigateToAddEditExercise(
                    exerciseName = null,
                    target = target,
                    navOptions = singleTopNavOptions
                )
            },
            onBackPress = navController::popBackStack
        )

        planEdit(navController::popBackStack) {
            navController.navigateToAddEditExercise(navOptions = singleTopNavOptions)
        }

        sessionDetail(navController::popBackStack)

        addEditExercise(navController::popBackStack)

        performance()

    }
}
