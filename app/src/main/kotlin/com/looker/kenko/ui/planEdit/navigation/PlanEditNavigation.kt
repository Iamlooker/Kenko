package com.looker.kenko.ui.planEdit.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
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
    onAddNewExerciseClick: () -> Unit,
) {
    composable<PlanEditRoute> {
        PlanEdit(
            onBackPress = onBackPress,
            onAddNewExerciseClick = onAddNewExerciseClick,
            viewModel = hiltViewModel()
        )
    }
}
