package com.looker.kenko.ui.exercises.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.ui.exercises.Exercises
import kotlinx.serialization.Serializable

@Serializable
object ExercisesRoute

fun NavController.navigateToExercises(navOptions: NavOptions? = null) {
    navigate(ExercisesRoute, navOptions = navOptions)
}

fun NavGraphBuilder.exercises(
    onExerciseClick: (id: Int?) -> Unit,
    onCreateClick: (target: MuscleGroups?) -> Unit,
    onBackPress: () -> Unit,
) {
    composable<ExercisesRoute> {
        Exercises(
            onExerciseClick = onExerciseClick,
            onCreateClick = onCreateClick,
            onBackPress = onBackPress,
            viewModel = hiltViewModel(),
        )
    }
}
