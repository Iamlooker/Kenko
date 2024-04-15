package com.looker.kenko.ui.plans.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.MuscleGroups.Biceps
import com.looker.kenko.data.model.MuscleGroups.Chest
import com.looker.kenko.data.model.MuscleGroups.Hamstrings
import com.looker.kenko.data.model.MuscleGroups.Quads
import com.looker.kenko.data.model.MuscleGroups.Shoulders
import com.looker.kenko.data.model.MuscleGroups.Triceps
import com.looker.kenko.data.model.MuscleGroups.UpperBack
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.model.sampleExercises
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.numbers
import kotlinx.datetime.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun PlanItem(
    plan: Plan,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .then(modifier)
            .clip(MaterialTheme.shapes.large)
            .background(
                if (plan.isActive) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.surfaceContainer
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = plan.name, style = MaterialTheme.typography.headlineMedium)
        plan.exercisesPerDay.forEach { (dayOfWeek, exercises) ->
            DayInPlan(dayOfWeek = dayOfWeek, exercises = exercises)
        }
    }
}

@Composable
private fun DayInPlan(
    dayOfWeek: DayOfWeek,
    exercises: List<Exercise>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val dayOfWeekTitle: String = remember(dayOfWeek) {
            dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        }
        Text(
            text = dayOfWeekTitle,
            style = MaterialTheme.typography.headlineSmall.numbers()
        )
        Column(
            modifier = Modifier
                .weight(1F)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = MaterialTheme.shapes.large,
                )
                .padding(12.dp)
        ) {
            exercises.forEach { exercise ->
                Text(text = exercise.name)
            }
        }
    }
}

@Preview
@Composable
private fun PlanItemPreview() {
    KenkoTheme {
        val plan = remember {
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
        }
        PlanItem(plan = plan)
    }
}
