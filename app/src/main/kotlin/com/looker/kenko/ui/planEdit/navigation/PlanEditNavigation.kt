package com.looker.kenko.ui.planEdit.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.looker.kenko.ui.planEdit.PlanEdit

const val PLAN_EDIT_ROUTE = "plan_edit"

const val ARG_PLAN_ID = "plan_id"

fun NavController.navigateToPlanEdit(id: Long? = null, navOptions: NavOptions? = null) {
    navigate("$PLAN_EDIT_ROUTE?id=${id ?: -1L}", navOptions)
}

fun NavGraphBuilder.planEdit(
    onBackPress: () -> Unit,
    onRequestNewExercise: () -> Unit,
) {
    composable(
        route = "$PLAN_EDIT_ROUTE?id={$ARG_PLAN_ID}",
        arguments = listOf(
            navArgument(name = ARG_PLAN_ID) {
                type = NavType.LongType
            }
        )
    ) {
        PlanEdit(onBackPress, onRequestNewExercise)
    }
}
