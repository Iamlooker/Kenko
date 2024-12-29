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
    val id: Int? = null,
    val target: String? = null,
)

fun NavController.navigateToAddEditExercise(
    id: Int? = null,
    target: MuscleGroups? = null,
    navOptions: NavOptions? = null,
) {
    navigate(AddEditExerciseRoute(id, target?.name), navOptions)
}

fun NavGraphBuilder.addEditExercise(
    onBackPress: () -> Unit,
) {
    composable<AddEditExerciseRoute> {
        AddEditExercise(onBackPress, onBackPress)
    }
}
