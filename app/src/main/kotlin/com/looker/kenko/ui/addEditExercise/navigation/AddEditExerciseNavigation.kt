package com.looker.kenko.ui.addEditExercise.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.ui.addEditExercise.AddEditExercise
import kotlinx.serialization.Serializable

@Serializable
data class AddEditExerciseRoute(
    val exerciseName: String? = null,
    val target: String? = null,
)

fun NavController.navigateToAddEditExercise(
    exerciseName: String? = null,
    target: MuscleGroups? = null,
    navOptions: NavOptions? = null,
) {
    navigate(AddEditExerciseRoute(exerciseName, target?.name), navOptions)
}

fun NavGraphBuilder.addEditExercise(
    onBackPress: () -> Unit,
) {
    composable<AddEditExerciseRoute> {
        AddEditExercise(onBackPress)
    }
}
