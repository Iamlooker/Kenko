package com.looker.kenko.ui.profile.navigation

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
    onNavigateToExercisesList: () -> Unit,
    onNavigateToAddExercise: () -> Unit,
    onNavigateToPlans: () -> Unit,
) {
    composable(PROFILE_ROUTE) {
        Profile(onNavigateToExercisesList, onNavigateToAddExercise, onNavigateToPlans)
    }
}
