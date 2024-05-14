package com.looker.kenko.ui.profile.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.ui.profile.Profile

const val PROFILE_ROUTE = "profile"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    navigate(PROFILE_ROUTE, navOptions)
}

fun NavGraphBuilder.profile(
    onExercisesClick: () -> Unit,
    onAddExerciseClick: () -> Unit,
    onPlanClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    composable(
        route = PROFILE_ROUTE
    ) {
        Profile(
            onExercisesClick = onExercisesClick,
            onAddExerciseClick = onAddExerciseClick,
            onPlanClick = onPlanClick,
            onSettingsClick = onSettingsClick,
            viewModel = hiltViewModel(),
        )
    }
}
