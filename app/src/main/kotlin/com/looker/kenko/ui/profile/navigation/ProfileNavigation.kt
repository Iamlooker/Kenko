package com.looker.kenko.ui.profile.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.ui.profile.Profile
import kotlinx.serialization.Serializable

@Serializable
object ProfileRoute

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    navigate(ProfileRoute, navOptions)
}

fun NavGraphBuilder.profile(
    onExercisesClick: () -> Unit,
    onAddExerciseClick: () -> Unit,
    onPlanClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    composable<ProfileRoute> {
        Profile(
            onExercisesClick = onExercisesClick,
            onAddExerciseClick = onAddExerciseClick,
            onPlanClick = onPlanClick,
            onSettingsClick = onSettingsClick,
            viewModel = hiltViewModel(),
        )
    }
}
