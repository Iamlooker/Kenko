package com.looker.kenko.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.ui.components.HealthQuotes
import com.looker.kenko.ui.helper.normalizeInt
import com.looker.kenko.ui.helper.plus
import com.looker.kenko.ui.helper.vertical
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.end
import com.looker.kenko.ui.theme.start

@Composable
fun Profile(
    onNavigateToExercisesList: () -> Unit,
    onNavigateToAddExercise: () -> Unit,
    onNavigateToPlans: () -> Unit,
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it + PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp))
                .verticalScroll(rememberScrollState())
        ) {
            Header()
            Spacer(modifier = Modifier.height(24.dp))
            if (state.isPlanAvailable) {
                CurrentPlanCard(
                    onEditClick = onNavigateToPlans,
                    name = state.planName,
                    content = {
                        Text(
                            text = stringResource(
                                R.string.label_plan_description,
                                state.numberOfExercisesPerPlan,
                                normalizeInt(state.workDays),
                                normalizeInt(state.restDays)
                            )
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            ExerciseCard(
                numberOfExercises = state.numberOfExercises,
                onAddClick = onNavigateToAddExercise,
                onExercisesClick = onNavigateToExercisesList
            )
            Spacer(modifier = Modifier.height(12.dp))
            LiftsCard(state.totalLifts)
            Spacer(modifier = Modifier.weight(1F))
            HealthQuotes(Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun Header() {
    Text(
        text = stringResource(R.string.label_settings_greeting),
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
private fun CurrentPlanCard(
    onEditClick: () -> Unit,
    name: String,
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.extraLarge
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 2.dp, end = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(imageVector = KenkoIcons.Plan, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Current Plan",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1F))
            FilledTonalIconButton(onClick = onEditClick) {
                Icon(imageVector = KenkoIcons.Rename, contentDescription = null)
            }
        }
        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 24.dp, bottom = 16.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.bodyLarge,
                    LocalContentColor provides MaterialTheme.colorScheme.outline,
                ) {
                    content()
                }
            }
            Icon(
                imageVector = KenkoIcons.Colony,
                contentDescription = null,
                modifier = Modifier.offset(x = 70.dp)
            )
        }
    }
}

@Composable
private fun ExerciseCard(
    numberOfExercises: Int,
    onAddClick: () -> Unit,
    onExercisesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            modifier = Modifier
                .weight(1F),
            shape = MaterialTheme.shapes.extraLarge.end(16.dp, 16.dp),
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary
            ),
            onClick = onExercisesClick
        ) {
            Row {
                Column(Modifier.padding(24.dp)) {
                    Text(
                        text = stringResource(R.string.label_exercise),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = numberOfExercises.toString(),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
        }
        val buttonCorner = MaterialTheme.shapes.extraLarge
            .start(16.dp, 16.dp)
        Box(
            modifier = Modifier
                .weight(1F)
                .fillMaxHeight()
                .clip(buttonCorner)
                .clickable(onClick = onAddClick)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = buttonCorner
                )
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = KenkoIcons.Add, contentDescription = null)
        }
    }
}

@Composable
private fun LiftsCard(setsPerformed: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.vertical(false),
                text = stringResource(R.string.label_lifts),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = setsPerformed.toString(),
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.weight(1F))
            Icon(
                imageVector = KenkoIcons.Reveal,
                tint = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentDescription = null,
                modifier = Modifier.offset(x = 30.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlanCard() {
    KenkoTheme {
        CurrentPlanCard(
            onEditClick = {
            },
            name = "Push-Pull-Leg",
            content = {
                Text(text = Typography.bullet + "21 exercises")
                Text(text = Typography.bullet + "05 days")
                Text(text = Typography.bullet + "02 rests")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExerciseCardPreview() {
    KenkoTheme {
        ExerciseCard(21, {}, {})
    }
}
