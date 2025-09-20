/*
 * Copyright (C) 2025. LooKeR & Contributors
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
import com.looker.kenko.ui.home.navigation.HomeRoute
import com.looker.kenko.ui.navigation.TopLevelDestinations
import com.looker.kenko.ui.navigation.TopLevelDestinations.Home
import com.looker.kenko.ui.navigation.TopLevelDestinations.Performance
import com.looker.kenko.ui.navigation.TopLevelDestinations.Profile
import com.looker.kenko.ui.navigation.supportedTopLevelDestinations
import com.looker.kenko.ui.performance.navigation.PerformanceRoute
import com.looker.kenko.ui.profile.navigation.ProfileRoute

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
            currentDestination?.hasRoute(HomeRoute::class) == true -> Home
            currentDestination?.hasRoute(ProfileRoute::class) == true -> Profile
            currentDestination?.hasRoute(PerformanceRoute::class) == true -> Performance
            else -> null
        }

    val isTopLevelDestination: Boolean
        @Composable get() = currentTopLevelDestination != null

    val topLevelDestinations: List<TopLevelDestinations> = supportedTopLevelDestinations

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestinations) {
        val route = topLevelDestination.route
        if (navController.currentDestination?.hasRoute(route::class) == true) return
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        navController.navigate(route = route, navOptions = topLevelNavOptions)
    }
}
