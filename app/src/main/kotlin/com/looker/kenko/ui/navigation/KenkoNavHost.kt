package com.looker.kenko.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
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
import com.looker.kenko.ui.home.navigation.home
import com.looker.kenko.ui.home.navigation.navigateToHome
import com.looker.kenko.ui.performance.navigation.performance
import com.looker.kenko.ui.planEdit.navigation.navigateToPlanEdit
import com.looker.kenko.ui.planEdit.navigation.planEdit
import com.looker.kenko.ui.plans.navigation.navigateToPlans
import com.looker.kenko.ui.plans.navigation.plans
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
        getStarted {
            navController.navigateToHome(splashNavOptions)
        }

        home(
            onSelectPlanClick = {
                navController.navigateToPlans(navOptions = singleTopNavOptions)
            },
            onAddExerciseClick = {
                navController.navigateToAddEditExercise(navOptions = singleTopNavOptions)
            },
            onExploreSessionsClick = {
                navController.navigateToSessions(navOptions = singleTopNavOptions)
            },
            onExploreExercisesClick = {
                navController.navigateToExercises(navOptions = singleTopNavOptions)
            },
            onStartSessionClick = {
                navController.navigateToSessionDetail(date = null, navOptions = singleTopNavOptions)
            },
            onCurrentPlanClick = {
                navController.navigateToPlanEdit(id = it, navOptions = singleTopNavOptions)
            },
        )

        sessions(
            onSessionClick = { date ->
                navController.navigateToSessionDetail(
                    date = date,
                    navOptions = singleTopNavOptions
                )
            },
            onBackPress = navController::popBackStackOnResume
        )

        plans(
            onPlanClick = {
                navController.navigateToPlanEdit(
                    id = it,
                    navOptions = singleTopNavOptions
                )
            },
            onBackPress = navController::popBackStackOnResume
        )

        settings(navController::popBackStackOnResume)

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
            onExerciseClick = { id ->
                navController.navigateToAddEditExercise(
                    id = id,
                    navOptions = singleTopNavOptions
                )
            },
            onCreateClick = { target ->
                navController.navigateToAddEditExercise(
                    target = target,
                    navOptions = singleTopNavOptions
                )
            },
            onBackPress = navController::popBackStackOnResume
        )

        planEdit(navController::popBackStackOnResume) {
            navController.navigateToAddEditExercise(navOptions = singleTopNavOptions)
        }

        sessionDetail(
            onBackPress = navController::popBackStackOnResume,
            onHistoryClick = navController::navigateToSessionDetail,
        )

        addEditExercise(navController::popBackStackOnResume)

        performance()

    }
}

private fun NavHostController.popBackStackOnResume() {
    if (lifecycleState?.isAtLeast(Lifecycle.State.RESUMED) == true) {
        popBackStack()
    }
}

private val NavHostController.lifecycleState: Lifecycle.State?
    get() = currentBackStackEntry?.lifecycle?.currentState
