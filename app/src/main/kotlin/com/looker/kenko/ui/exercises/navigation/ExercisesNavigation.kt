package com.looker.kenko.ui.exercises.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.ui.exercises.Exercises
import kotlinx.serialization.Serializable

const val EXERCISES_ROUTE = "exercises"

@Serializable
object ExercisesRoute

fun NavController.navigateToExercises(navOptions: NavOptions? = null) {
    navigate(EXERCISES_ROUTE, navOptions = navOptions)
}

fun NavGraphBuilder.exercises(
    onExerciseClick: (name: String) -> Unit,
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
