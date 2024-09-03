package com.looker.kenko.ui.plans.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.ui.plans.Plan
import kotlinx.serialization.Serializable

@Serializable
object PlanRoute

fun NavController.navigateToPlans(navOptions: NavOptions? = null) {
    navigate(PlanRoute, navOptions)
}

fun NavGraphBuilder.plans(
    onPlanClick: (Int?) -> Unit,
    onBackPress: () -> Unit,
) {
    composable<PlanRoute> {
        Plan(
            onPlanClick = onPlanClick,
            onBackPress = onBackPress,
            viewModel = hiltViewModel(),
        )
    }
}
