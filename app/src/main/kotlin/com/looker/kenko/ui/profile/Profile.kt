/*
 * Copyright (C) 2025. LooKeR & Contributors
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.looker.kenko.ui.profile

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.model.PlanStat
import com.looker.kenko.ui.components.HealthQuotes
import com.looker.kenko.ui.components.KenkoBorderWidth
import com.looker.kenko.ui.components.OutlineBorder
import com.looker.kenko.ui.components.SecondaryBorder
import com.looker.kenko.ui.extensions.PHI
import com.looker.kenko.ui.extensions.normalizeInt
import com.looker.kenko.ui.extensions.plus
import com.looker.kenko.ui.extensions.vertical
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.end
import com.looker.kenko.ui.theme.numbers
import com.looker.kenko.ui.theme.start

@Composable
fun Profile(
    viewModel: ProfileViewModel,
    onExercisesClick: () -> Unit,
    onAddExerciseClick: () -> Unit,
    onPlanClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Profile(
        state = state,
        onSettingsClick = onSettingsClick,
        onPlanClick = onPlanClick,
        onAddExerciseClick = onAddExerciseClick,
        onExercisesClick = onExercisesClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Profile(
    state: ProfileUiState,
    onSettingsClick: () -> Unit,
    onPlanClick: () -> Unit,
    onAddExerciseClick: () -> Unit,
    onExercisesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.label_profile))
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(painter = KenkoIcons.Settings, contentDescription = null)
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding + PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp))
                .verticalScroll(rememberScrollState()),
        ) {
            if (state.isPlanAvailable) {
                CurrentPlanCard(
                    onPlanClick = onPlanClick,
                    name = state.planName,
                    content = {
                        Text(
                            text = stringResource(
                                R.string.label_plan_description,
                                state.planStat!!.exercises,
                                normalizeInt(state.planStat.workDays),
                                normalizeInt(state.planStat.restDays),
                            ),
                        )
                    },
                )
            } else {
                SelectPlanCard(onPlanClick)
            }
            Spacer(modifier = Modifier.height(12.dp))
            ExerciseCard(
                numberOfExercises = state.numberOfExercises,
                onAddClick = onAddExerciseClick,
                onExercisesClick = onExercisesClick,
            )
            Spacer(modifier = Modifier.height(12.dp))
            LiftsCard(state.totalLifts)
            Spacer(modifier = Modifier.weight(1F))
            HealthQuotes(Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
private fun CurrentPlanCard(
    onPlanClick: () -> Unit,
    name: String,
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(PHI),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = MaterialTheme.shapes.extraLarge,
        border = SecondaryBorder,
        onClick = onPlanClick,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 2.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(painter = KenkoIcons.Plan, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.label_current_plan),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.weight(1F))
                FilledIconButton(onClick = onPlanClick) {
                    Icon(painter = KenkoIcons.Rename, contentDescription = null)
                }
            }
            HorizontalDivider(
                thickness = KenkoBorderWidth,
                color = MaterialTheme.colorScheme.secondary,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 24.dp, bottom = 16.dp),
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge,
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
                    imageVector = KenkoIcons.Stack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.offset(x = 0.dp),
                )
            }
        }
    }
}

@Composable
fun SelectPlanCard(
    onSelectPlanClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(onClick = onSelectPlanClick)
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.label_select_plan),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Icon(
            painter = KenkoIcons.ArrowOutward,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            contentDescription = null,
        )
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
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val cardShape = MaterialTheme.shapes.extraLarge
        val surfaceShape = remember(cardShape) {
            cardShape.end(16.dp, 16.dp)
        }
        Surface(
            modifier = Modifier.weight(1.5F),
            shape = surfaceShape,
            border = OutlineBorder,
            onClick = onExercisesClick,
        ) {
            Column(Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(R.string.label_exercise),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = numberOfExercises.toString(),
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
        }
        val buttonShape = remember(cardShape) {
            cardShape.start(16.dp, 16.dp)
        }
        Box(
            modifier = Modifier
                .weight(1F)
                .fillMaxHeight()
                .clip(buttonShape)
                .clickable(onClick = onAddClick)
                .border(
                    border = SecondaryBorder,
                    shape = buttonShape,
                )
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = KenkoIcons.Add,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                contentDescription = stringResource(R.string.label_add),
            )
        }
    }
}

@Composable
private fun LiftsCard(setsPerformed: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        border = SecondaryBorder,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.vertical(false),
                text = stringResource(R.string.label_lifts),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = setsPerformed.toString(),
                style = MaterialTheme.typography.displayLarge.numbers(),
            )
            Spacer(modifier = Modifier.weight(1F))
            Icon(
                imageVector = KenkoIcons.Reveal,
                tint = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentDescription = null,
                modifier = Modifier.offset(x = 30.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlanCard() {
    KenkoTheme {
        CurrentPlanCard(
            onPlanClick = {
            },
            name = "Push-Pull-Leg",
            content = {
                Text(
                    text = stringResource(
                        R.string.label_plan_description,
                        12,
                        normalizeInt(5),
                        normalizeInt(2),
                    ),
                )
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyPlanCardPreview() {
    KenkoTheme {
        SelectPlanCard({})
    }
}

@Preview(showBackground = true)
@Composable
private fun ExerciseCardPreview() {
    KenkoTheme {
        ExerciseCard(21, {}, {})
    }
}

@Preview
@Composable
private fun ProfileNoPlanPreview() {
    KenkoTheme {
        Profile(
            state = ProfileUiState(12, false, "Push-Pull-Leg", 2, PlanStat(12, 5)),
            onSettingsClick = { },
            onPlanClick = { },
            onAddExerciseClick = { },
            onExercisesClick = { },
        )
    }
}

@Preview
@Composable
private fun ProfilePreview() {
    KenkoTheme {
        Profile(
            state = ProfileUiState(12, true, "Push-Pull-Leg", 2, PlanStat(12, 5)),
            onSettingsClick = { },
            onPlanClick = { },
            onAddExerciseClick = { },
            onExercisesClick = { },
        )
    }
}
