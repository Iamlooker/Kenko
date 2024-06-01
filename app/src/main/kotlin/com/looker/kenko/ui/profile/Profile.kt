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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
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
import com.looker.kenko.ui.components.HealthQuotes
import com.looker.kenko.ui.components.KenkoBorderWidth
import com.looker.kenko.ui.components.OnSurfaceVariantBorder
import com.looker.kenko.ui.components.SecondaryBorder
import com.looker.kenko.ui.extensions.PHI
import com.looker.kenko.ui.extensions.normalizeInt
import com.looker.kenko.ui.extensions.plus
import com.looker.kenko.ui.extensions.vertical
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.end
import com.looker.kenko.ui.theme.start
import kotlinx.coroutines.delay

@Composable
fun Profile(
    viewModel: ProfileViewModel,
    onExercisesClick: () -> Unit,
    onAddExerciseClick: () -> Unit,
    onPlanClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        delay(2_000)
        viewModel.completeOnboarding()
    }

    Profile(
        state = state,
        onSettingsClick = onSettingsClick,
        onPlanClick = onPlanClick,
        onAddExerciseClick = onAddExerciseClick,
        onExercisesClick = onExercisesClick,
    )
}

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
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding + PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp))
                .verticalScroll(rememberScrollState())
        ) {
            Header(onSettingsClick = onSettingsClick)
            Spacer(modifier = Modifier.height(24.dp))
            if (state.isPlanAvailable) {
                CurrentPlanCard(
                    onPlanClick = onPlanClick,
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
            } else {
                SelectPlanCard(onPlanClick)
            }
            Spacer(modifier = Modifier.height(12.dp))
            ExerciseCard(
                numberOfExercises = state.numberOfExercises,
                onAddClick = onAddExerciseClick,
                onExercisesClick = onExercisesClick
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
private fun Header(
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.label_profile_greeting),
            style = MaterialTheme.typography.headlineMedium
        )
        IconButton(onClick = onSettingsClick) {
            Icon(imageVector = KenkoIcons.Settings, contentDescription = null)
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
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .border(
                border = OnSurfaceVariantBorder,
                shape = MaterialTheme.shapes.extraLarge
            )
            .clickable(onClick = onPlanClick),
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
                text = stringResource(R.string.label_current_plan),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1F))
            FilledTonalIconButton(onClick = onPlanClick) {
                Icon(imageVector = KenkoIcons.Rename, contentDescription = null)
            }
        }
        HorizontalDivider(
            thickness = KenkoBorderWidth,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
fun SelectPlanCard(
    onSelectPlanClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(PHI)
            .clip(MaterialTheme.shapes.extraLarge)
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
            imageVector = KenkoIcons.ArrowOutwardLarge,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            contentDescription = null
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
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val cardShape = MaterialTheme.shapes.extraLarge
        val surfaceShape = remember(cardShape) {
            cardShape.end(16.dp, 16.dp)
        }
        Surface(
            modifier = Modifier.weight(1.5F),
            shape = surfaceShape,
            border = OnSurfaceVariantBorder,
            onClick = onExercisesClick
        ) {
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
                    shape = buttonShape
                )
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = KenkoIcons.Add,
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
        border = OnSurfaceVariantBorder,
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
            onPlanClick = {
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
private fun ProfilePreview() {
    KenkoTheme {
        Profile(
            state = ProfileUiState(12, 12, true, "Push-Pull-Leg", 2, 5, 12),
            onSettingsClick = { },
            onPlanClick = { },
            onAddExerciseClick = { },
            onExercisesClick = { },
        )
    }
}
