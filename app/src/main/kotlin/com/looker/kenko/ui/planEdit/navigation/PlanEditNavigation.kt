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

package com.looker.kenko.ui.planEdit.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.ui.planEdit.PlanEdit
import kotlinx.serialization.Serializable

@Serializable
data class PlanEditRoute(
    val id: Int,
)

fun NavController.navigateToPlanEdit(id: Int = -1, navOptions: NavOptions? = null) {
    navigate(PlanEditRoute(id), navOptions)
}

fun NavGraphBuilder.planEdit(
    onBackPress: () -> Unit,
    onAddNewExerciseClick: (name: String?, target: MuscleGroups?) -> Unit,
) {
    composable<PlanEditRoute> {
        PlanEdit(
            onBackPress = onBackPress,
            onAddNewExerciseClick = onAddNewExerciseClick,
            viewModel = hiltViewModel()
        )
    }
}
