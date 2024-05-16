package com.looker.kenko.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.looker.kenko.ui.navigation.TopLevelDestinations
import com.looker.kenko.ui.navigation.TopLevelDestinations.Home
import com.looker.kenko.ui.navigation.TopLevelDestinations.Performance
import com.looker.kenko.ui.navigation.TopLevelDestinations.Profile
import com.looker.kenko.ui.performance.navigation.PerformanceRoute
import com.looker.kenko.ui.performance.navigation.navigateToPerformance
import com.looker.kenko.ui.profile.navigation.ProfileRoute
import com.looker.kenko.ui.profile.navigation.navigateToProfile
import com.looker.kenko.ui.sessions.navigation.SessionRoute
import com.looker.kenko.ui.sessions.navigation.navigateToSessions

@Composable
fun rememberKenkoAppState(
    navController: NavController = rememberNavController(),
): KenkoAppState = remember(navController) {
    KenkoAppState(navController)
}

@Stable
class KenkoAppState(
    val navController: NavController,
) {

    private val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestinations?
        @Composable get() = when {
            currentDestination == null -> null
            currentDestination?.hasRoute(SessionRoute::class) == true -> Home
            currentDestination?.hasRoute(ProfileRoute::class) == true -> Profile
            currentDestination?.hasRoute(PerformanceRoute::class) == true -> Performance
            else -> null
        }

    val isTopLevelDestination: Boolean
        @Composable get() = currentTopLevelDestination != null

    val topLevelDestinations: List<TopLevelDestinations> = TopLevelDestinations.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestinations) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        when (topLevelDestination) {
            Performance -> navController.navigateToPerformance(topLevelNavOptions)
            Home -> navController.navigateToSessions(topLevelNavOptions)
            Profile -> navController.navigateToProfile(topLevelNavOptions)
        }
    }
}
