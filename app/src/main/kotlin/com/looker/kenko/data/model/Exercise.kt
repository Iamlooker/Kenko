package com.looker.kenko.data.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.looker.kenko.R
import com.looker.kenko.data.model.MuscleGroups.Biceps
import com.looker.kenko.data.model.MuscleGroups.Calves
import com.looker.kenko.data.model.MuscleGroups.Chest
import com.looker.kenko.data.model.MuscleGroups.Core
import com.looker.kenko.data.model.MuscleGroups.Glutes
import com.looker.kenko.data.model.MuscleGroups.Hamstrings
import com.looker.kenko.data.model.MuscleGroups.Lats
import com.looker.kenko.data.model.MuscleGroups.Quads
import com.looker.kenko.data.model.MuscleGroups.Shoulders
import com.looker.kenko.data.model.MuscleGroups.Traps
import com.looker.kenko.data.model.MuscleGroups.Triceps
import com.looker.kenko.data.model.MuscleGroups.UpperBack

@Immutable
data class Exercise(
    val name: String,
    val target: MuscleGroups,
    val reference: String? = null,
    val isIsometric: Boolean = false,
    val id: Int? = null,
)

@Stable
val Exercise.repDurationStringRes: Int
    @StringRes
    get() = if (isIsometric) R.string.label_duration else R.string.label_reps

class ExercisesPreviewParameter : PreviewParameterProvider<List<Exercise>> {
    override val values = sequenceOf(
        listOf(
            Exercise("Curls", Biceps),
            Exercise("Barbell Curls", Biceps),
            Exercise("Preacher Curls", Biceps),
            Exercise("B-t-B Curls", Biceps),
        ),
        listOf(
            Exercise("Push-down", Triceps),
            Exercise("Skull-Crushers", Triceps),
            Exercise("Push-overs", Triceps),
        ),
        listOf(
            Exercise("Lateral Raises", Shoulders),
            Exercise("Shoulder Press", Shoulders),
            Exercise("Face Pulls", Shoulders),
        ),
        listOf(
            Exercise("Squats", Quads),
            Exercise("Leg Press", Quads),
            Exercise("Hack Squats", Quads),
            Exercise("Leg Extensions", Quads),
        ),
        listOf(
            Exercise("SDL", Hamstrings),
            Exercise("Lying Leg Curls", Hamstrings),
        ),
        listOf(Exercise("Calve Raises", Calves)),
        listOf(
            Exercise("Hip Thrusts", Glutes),
            Exercise("Lunges", Glutes),
        ),
        listOf(
            Exercise("Sit-ups", Core),
            Exercise("Leg Raises", Core),
        ),
        listOf(
            Exercise("Bench Press", Chest),
            Exercise("Incline Bench", Chest),
            Exercise("Pec Dec", Chest),
            Exercise("Chest Fly", Chest),
        ),
        listOf(Exercise("Shrugs", Traps)),
        listOf(
            Exercise("Lat Pull-down", Lats),
            Exercise("Pull-ups", Lats),
            Exercise("Lat Prayers", Lats),
        ),
        listOf(
            Exercise("Bent-over Rows", UpperBack),
            Exercise("Chest-Supported Rows", UpperBack),
            Exercise("Rows", UpperBack),
        ),
    )
}
