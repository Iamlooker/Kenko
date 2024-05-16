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
    val id: Long,
)

fun NavController.navigateToPlanEdit(id: Long? = null, navOptions: NavOptions? = null) {
    navigate(PlanEditRoute(id ?: -1L), navOptions)
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
