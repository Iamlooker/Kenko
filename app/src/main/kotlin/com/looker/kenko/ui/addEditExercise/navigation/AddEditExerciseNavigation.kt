package com.looker.kenko.ui.addEditExercise.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.looker.kenko.ui.addEditExercise.AddEditExercise

const val ADD_EDIT_EXERCISE_ROUTE = "add_edit_exercise"

const val ARG_EXERCISE_NAME = "exercise_name"

fun NavController.navigateToAddEditExercise(exerciseName: String?, navOptions: NavOptions? = null) {
    navigate("$ADD_EDIT_EXERCISE_ROUTE/?name=${exerciseName}", navOptions)
}

fun NavGraphBuilder.addEditExercise(
    onBackPress: () -> Unit,
) {
    composable(
        route = "$ADD_EDIT_EXERCISE_ROUTE/?name={$ARG_EXERCISE_NAME}",
        arguments = listOf(
            navArgument(name = ARG_EXERCISE_NAME) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            }
        )
    ) {
        AddEditExercise(onBackPress)
    }
}
