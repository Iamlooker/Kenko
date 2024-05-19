package com.looker.kenko.ui.home.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.ui.home.Home
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HomeRoute, navOptions = navOptions)
}

fun NavGraphBuilder.home(
    onSelectPlanClick: () -> Unit,
    onAddExerciseClick: () -> Unit,
    onExploreSessionsClick: () -> Unit,
    onExploreExercisesClick: () -> Unit,
    onStartSessionClick: () -> Unit,
    onCurrentPlanClick: (Long) -> Unit,
) {
    composable<HomeRoute> {
        Home(
            onSelectPlanClick = onSelectPlanClick,
            onAddExerciseClick = onAddExerciseClick,
            onExploreSessionsClick = onExploreSessionsClick,
            onExploreExercisesClick = onExploreExercisesClick,
            onStartSessionClick = onStartSessionClick,
            onCurrentPlanClick = onCurrentPlanClick,
            viewModel = hiltViewModel(),
        )
    }
}
