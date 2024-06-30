package com.looker.kenko.data.model.mock

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.looker.kenko.data.model.MuscleGroups.Biceps
import com.looker.kenko.data.model.MuscleGroups.Chest
import com.looker.kenko.data.model.MuscleGroups.Hamstrings
import com.looker.kenko.data.model.MuscleGroups.Quads
import com.looker.kenko.data.model.MuscleGroups.Shoulders
import com.looker.kenko.data.model.MuscleGroups.Triceps
import com.looker.kenko.data.model.MuscleGroups.UpperBack
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.model.sampleExercises
import kotlinx.datetime.DayOfWeek

class PlanProvider: PreviewParameterProvider<Plan> {
    override val values: Sequence<Plan>
        get() = sequenceOf(
            Plan(
                name = "Push Pull Leg",
                exercisesPerDay = mapOf(
                    DayOfWeek.MONDAY to Chest.sampleExercises + Triceps.sampleExercises,
                    DayOfWeek.TUESDAY to UpperBack.sampleExercises + Biceps.sampleExercises,
                    DayOfWeek.WEDNESDAY to Quads.sampleExercises + Hamstrings.sampleExercises
                            + Shoulders.sampleExercises,
                    DayOfWeek.THURSDAY to Chest.sampleExercises + Triceps.sampleExercises,
                    DayOfWeek.FRIDAY to UpperBack.sampleExercises + Biceps.sampleExercises,
                ).mapValues { it.value.shuffled().take(5) },
                isActive = true
            )
        )
}