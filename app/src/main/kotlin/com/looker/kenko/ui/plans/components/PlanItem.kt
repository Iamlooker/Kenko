package com.looker.kenko.ui.plans.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.looker.kenko.R
import com.looker.kenko.data.model.MuscleGroups.Biceps
import com.looker.kenko.data.model.MuscleGroups.Chest
import com.looker.kenko.data.model.MuscleGroups.Hamstrings
import com.looker.kenko.data.model.MuscleGroups.Quads
import com.looker.kenko.data.model.MuscleGroups.Shoulders
import com.looker.kenko.data.model.MuscleGroups.Triceps
import com.looker.kenko.data.model.MuscleGroups.UpperBack
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.model.sampleExercises
import com.looker.kenko.data.model.stats
import com.looker.kenko.ui.extensions.normalizeInt
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
    val transition = updateTransition(targetState = plan.isActive, label = null)
    val background by transition.animateColor(label = "background") {
        if (it) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.surfaceContainer
    }
    val contentColor by transition.animateColor(label = "foreground") {
        if (it) MaterialTheme.colorScheme.onSecondaryContainer
        else MaterialTheme.colorScheme.onSurface
    }

    Surface(
        onClick = onClick,
        color = background,
        contentColor = contentColor,
    ) {
        Column(
            modifier = modifier.padding(16.dp),
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
            AnimatedVisibility(visible = plan.isActive) {
                Text(
                    text = ".selected",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
            if (plan.isActive) {
                Spacer(modifier = Modifier.height(4.dp))
            }
            val stats = remember(plan) { plan.stats }
            Text(
                text = stringResource(
                    R.string.label_plan_description,
                    stats.exercises,
                    normalizeInt(stats.workDays),
                    normalizeInt(stats.restDays)
                )
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PlanItemPreview() {
    KenkoTheme {
        var plan by remember {
            mutableStateOf(
                Plan(
                    name = "Push Pull Leg",
                    exercisesPerDay = mapOf(
                        DayOfWeek.MONDAY to Chest.sampleExercises + Triceps.sampleExercises,
                        DayOfWeek.TUESDAY to UpperBack.sampleExercises + Biceps.sampleExercises,
                        DayOfWeek.WEDNESDAY to Quads.sampleExercises + Hamstrings.sampleExercises,
                        DayOfWeek.THURSDAY to Chest.sampleExercises + Triceps.sampleExercises,
                        DayOfWeek.FRIDAY to UpperBack.sampleExercises + Biceps.sampleExercises,
                    ),
                    isActive = false
                )
            )
        }
        PlanItem(plan = plan, { plan = plan.copy(isActive = it) }, {})
    }
}

@PreviewLightDark
@Composable
private fun PlanItemInActivePreview() {
    KenkoTheme {
        PlanItem(plan = Plan(
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
        ), {}, {})
    }
}
