package com.looker.kenko.ui.addEditExercise.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.ui.addEditExercise.AddEditExercise
import kotlinx.serialization.Serializable

const val ADD_EDIT_EXERCISE_ROUTE = "add_edit_exercise"

const val ARG_EXERCISE_NAME = "exercise_name"
const val ARG_TARGET_NAME = "target_name"

@Serializable
data class AddEditExerciseRoute(
    val exerciseName: String? = null,
    val target: MuscleGroups? = null,
)

fun NavController.navigateToAddEditExercise(
    exerciseName: String? = null,
    target: MuscleGroups? = null,
    navOptions: NavOptions? = null,
) {
    navigate(AddEditExerciseRoute(exerciseName, target), navOptions)
}

fun NavGraphBuilder.addEditExercise(
    onBackPress: () -> Unit,
) {
    composable<AddEditExerciseRoute> {
        AddEditExercise(onBackPress)
    }
}
