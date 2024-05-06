package com.looker.kenko.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.looker.kenko.ui.KenkoAppState
import com.looker.kenko.ui.addEditExercise.navigation.addEditExercise
import com.looker.kenko.ui.addEditExercise.navigation.navigateToAddEditExercise
import com.looker.kenko.ui.getStarted.navigation.GET_STARTED_ROUTE
import com.looker.kenko.ui.getStarted.navigation.getStarted
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
        getStarted {
            navController.navigateToSessions(
                navOptions = navOptions {
                    launchSingleTop = true
                }
            )
        }

        sessions {
            navController.navigateToSessionDetail(
                date = it,
                navOptions = navOptions {
                    launchSingleTop = true
                }
            )
        }

        plans {
            navController.navigateToPlanEdit(
                id = it,
                navOptions = navOptions {
                    launchSingleTop = true
                }
            )
        }

        profile(
            onNavigateToAddExercise = {
                navController.navigateToAddEditExercise(
                    navOptions = navOptions {
                        launchSingleTop = true
                    }
                )
            },
            onNavigateToExercisesList = {
                navController.navigateToAddEditExercise(
                    navOptions = navOptions {
                        launchSingleTop = true
                    }
                )
            },
            onNavigateToPlans = {
                navController.navigateToPlans(
                    navOptions = navOptions {
                        launchSingleTop = true
                    }
                )
            },
        )

        planEdit(navController::popBackStack) {
            navController.navigateToAddEditExercise(
                navOptions = navOptions {
                    launchSingleTop = true
                }
            )
        }

        sessionDetail(navController::popBackStack)

        addEditExercise(navController::popBackStack)

        performance()

    }
}
