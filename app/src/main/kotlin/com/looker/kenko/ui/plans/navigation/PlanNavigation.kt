package com.looker.kenko.ui.plans.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.ui.plans.Plan

const val PLAN_ROUTE = "plan_list"

fun NavController.navigateToPlans(navOptions: NavOptions? = null) {
    navigate(PLAN_ROUTE, navOptions)
}

fun NavGraphBuilder.plans(
    onNavigateToAddPage: (Long?) -> Unit
) {
    composable(
        route = PLAN_ROUTE,
    ) {
        Plan(viewModel = hiltViewModel(), onNavigateToAddPage = onNavigateToAddPage)
    }
}
