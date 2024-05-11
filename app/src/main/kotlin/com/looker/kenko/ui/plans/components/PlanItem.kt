package com.looker.kenko.ui.plans.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
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
import com.looker.kenko.ui.planEdit.components.kenkoDayName
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import kotlinx.datetime.DayOfWeek

@Composable
fun PlanItem(
    plan: Plan,
    onActiveChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        color = if (plan.isActive) MaterialTheme.colorScheme.surfaceContainerHigh
        else MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .then(modifier)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = plan.name, style = MaterialTheme.typography.headlineMedium)
                OutlinedIconToggleButton(
                    checked = plan.isActive,
                    onCheckedChange = onActiveChange
                ) {
                    Icon(imageVector = KenkoIcons.Done, contentDescription = null)
                }
            }
            plan.exercisesPerDay.forEach { (dayOfWeek, exercises) ->
                DayInPlan(dayOfWeek = dayOfWeek, exercises = exercises)
            }
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
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val dayOfWeekTitle: String = kenkoDayName(dayOfWeek)
        val exercisesMarquee = remember { exercises.joinToString { Typography.bullet + it.name } }
        Text(
            text = dayOfWeekTitle,
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = exercisesMarquee,
            modifier = Modifier.basicMarquee(),
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

@PreviewLightDark
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
        PlanItem(plan = plan, {}, {})
    }
}

@PreviewLightDark
@Composable
private fun PlanItemInActivePreview() {
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
                isActive = false
            )
        }
        PlanItem(plan = plan, {}, {})
    }
}
