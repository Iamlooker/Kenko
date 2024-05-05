package com.looker.kenko.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.looker.kenko.ui.navigation.TopLevelDestinations

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

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDesination: TopLevelDestinations?
        @Composable get() = when (currentDestination?.route) {
            TopLevelDestinations.Home.route -> TopLevelDestinations.Home
            TopLevelDestinations.Profile.route -> TopLevelDestinations.Profile
            TopLevelDestinations.Performance.route -> TopLevelDestinations.Performance
            else -> null
        }

    val isTopLevelDestination: Boolean
        @Composable get() = currentTopLevelDesination != null

    val topLevelDestinations: List<TopLevelDestinations> = TopLevelDestinations.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestinations) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        navController.navigate(topLevelDestination.route, topLevelNavOptions)
    }
}