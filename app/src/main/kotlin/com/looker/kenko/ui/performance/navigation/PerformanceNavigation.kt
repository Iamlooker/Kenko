/*
 * Copyright (C) 2025 LooKeR & Contributors
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

package com.looker.kenko.ui.performance.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.ui.performance.Performance
import kotlinx.serialization.Serializable

@Serializable
object PerformanceRoute

fun NavController.navigateToPerformance(navOptions: NavOptions? = null) {
    navigate(PerformanceRoute, navOptions)
}

fun NavGraphBuilder.performance(
    onAddNewExercise: () -> Unit,
) {
    composable<PerformanceRoute> {
        Performance(
            viewModel = hiltViewModel(),
            onAddNewExercise = onAddNewExercise,
        )
    }
}
