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

package com.looker.kenko.ui.home.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.ui.home.Home
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HomeRoute, navOptions = navOptions)
}

fun NavGraphBuilder.home(
    onSelectPlanClick: () -> Unit,
    onAddExerciseClick: () -> Unit,
    onExploreSessionsClick: () -> Unit,
    onExploreExercisesClick: () -> Unit,
    onStartSessionClick: () -> Unit,
    onCurrentPlanClick: (Int) -> Unit,
) {
    composable<HomeRoute> {
        Home(
            onSelectPlanClick = onSelectPlanClick,
            onAddExerciseClick = onAddExerciseClick,
            onExploreSessionsClick = onExploreSessionsClick,
            onExploreExercisesClick = onExploreExercisesClick,
            onStartSessionClick = onStartSessionClick,
            onCurrentPlanClick = onCurrentPlanClick,
            viewModel = hiltViewModel(),
        )
    }
}
