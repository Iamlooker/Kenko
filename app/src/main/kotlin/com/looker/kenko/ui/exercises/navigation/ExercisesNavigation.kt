package com.looker.kenko.ui.exercises.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.ui.exercises.Exercises

const val EXERCISES_ROUTE = "exercises"

fun NavController.navigateToExercises(navOptions: NavOptions? = null) {
    navigate(EXERCISES_ROUTE, navOptions = navOptions)
}

fun NavGraphBuilder.exercises(onExerciseClick: (String?) -> Unit) {
    composable(
        route = EXERCISES_ROUTE
    ) {
        Exercises(onNavigateToExercise = onExerciseClick)
    }
}
