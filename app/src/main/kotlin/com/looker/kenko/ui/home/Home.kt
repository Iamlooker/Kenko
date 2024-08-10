package com.looker.kenko.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.ui.components.KenkoBorderWidth
import com.looker.kenko.ui.components.LiftingQuotes
import com.looker.kenko.ui.components.OutlineBorder
import com.looker.kenko.ui.components.SecondaryBorder
import com.looker.kenko.ui.extensions.PHI
import com.looker.kenko.ui.extensions.plus
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun Home(
    viewModel: HomeViewModel,
    onSelectPlanClick: () -> Unit,
    onAddExerciseClick: () -> Unit,
    onExploreSessionsClick: () -> Unit,
    onExploreExercisesClick: () -> Unit,
    onStartSessionClick: () -> Unit,
    onCurrentPlanClick: (Long) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Home(
        state = state,
        onSelectPlanClick = onSelectPlanClick,
        onAddExerciseClick = onAddExerciseClick,
        onExploreSessionsClick = onExploreSessionsClick,
        onExploreExercisesClick = onExploreExercisesClick,
        onStartSessionClick = onStartSessionClick,
        onCurrentPlanClick = onCurrentPlanClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Home(
    state: HomeUiData,
    onSelectPlanClick: () -> Unit = {},
    onAddExerciseClick: () -> Unit = {},
    onExploreSessionsClick: () -> Unit = {},
    onExploreExercisesClick: () -> Unit = {},
    onStartSessionClick: () -> Unit = {},
    onCurrentPlanClick: (Long) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.label_home))
                },
                actions = {
                    if (state.hasMultipleSessions) {
                        IconButton(onClick = onExploreSessionsClick) {
                            Icon(
                                imageVector = KenkoIcons.History,
                                contentDescription = null,
                            )
                        }
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding + PaddingValues(bottom = 80.dp)),
            horizontalAlignment = CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = !state.isPlanSelected,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
            ) {
                SelectPlanTicker()
            }
            if (!state.isPlanSelected) {
                SelectPlan(onSelectPlanClick = onSelectPlanClick)
            } else {
                Category {
                    StartSessionCard(
                        isSessionStarted = state.isSessionStarted,
                        onClick = onStartSessionClick,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Category {
                    ExploreExercisesCard(
                        onClick = onExploreExercisesClick
                    )
                    AddExerciseCard(
                        onClick = onAddExerciseClick
                    )
                }
                Spacer(modifier = Modifier.weight(1F))
            }
            LiftingQuotes()
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun ColumnScope.SelectPlan(
    onSelectPlanClick: () -> Unit,
) {
    Spacer(modifier = Modifier.weight(1F))
    Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = stringResource(R.string.label__selecting_a_plan),
        style = MaterialTheme.typography.displayLarge.copy(
            lineBreak = LineBreak.Heading
        ),
        color = MaterialTheme.colorScheme.tertiary,
    )
    Spacer(modifier = Modifier.weight(1F))
    FilledTonalButton(
        onClick = onSelectPlanClick,
        contentPadding = PaddingValues(
            vertical = 24.dp,
            horizontal = 40.dp
        )
    ) {
        Text(text = stringResource(R.string.label_select_plan_one))
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            imageVector = KenkoIcons.ArrowOutward,
            contentDescription = ""
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun Category(
    modifier: Modifier = Modifier,
    content: @Composable() (RowScope.() -> Unit),
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(modifier = Modifier)
            content()
            Spacer(modifier = Modifier)
        }
    }
}

private fun Modifier.cardWidth() = width(310.dp)

@Composable
private fun SelectPlanTicker(
    modifier: Modifier = Modifier
) {
    val tickerText = stringResource(R.string.label_select_a_plan)
    val tickerMarquee = remember {
        List(10) {
            tickerText
        }.joinToString(
            separator = " ${Typography.bullet} ",
            postfix = " ${Typography.bullet} ",
        )
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = KenkoBorderWidth,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .basicMarquee(initialDelayMillis = 0),
            text = tickerMarquee,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.outline,
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = KenkoBorderWidth,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun StartSessionCard(
    isSessionStarted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .cardWidth()
            .aspectRatio(PHI)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable(onClick = onClick)
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val stringRes = remember(isSessionStarted) {
            if (isSessionStarted) {
                R.string.label_continue_session
            } else {
                R.string.label_start_session
            }
        }
        Text(
            text = stringResource(stringRes),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
        Icon(
            imageVector = KenkoIcons.ArrowOutwardLarge,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            contentDescription = null,
        )
    }
}

@Composable
private fun ExploreSessionsCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .cardWidth()
            .aspectRatio(PHI)
            .clip(MaterialTheme.shapes.extraLarge)
            .border(SecondaryBorder, MaterialTheme.shapes.extraLarge)
            .clickable(onClick = onClick)
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.label_explore_sessions),
            style = MaterialTheme.typography.headlineLarge,
        )
        Icon(
            imageVector = KenkoIcons.QuarterCircles,
            tint = MaterialTheme.colorScheme.outline,
            contentDescription = null,
        )
    }
}

@Composable
private fun ExploreExercisesCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .cardWidth()
            .aspectRatio(PHI)
            .clip(MaterialTheme.shapes.extraLarge)
            .border(SecondaryBorder, MaterialTheme.shapes.extraLarge)
            .clickable(onClick = onClick)
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.label_explore_exercises),
            style = MaterialTheme.typography.headlineLarge,
        )
        Icon(
            modifier = Modifier.offset(x = 40.dp),
            imageVector = KenkoIcons.Wireframe,
            contentDescription = null,
        )
    }
}

@Composable
private fun AddExerciseCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .cardWidth()
            .aspectRatio(PHI)
            .clip(MaterialTheme.shapes.extraLarge)
            .border(SecondaryBorder, MaterialTheme.shapes.extraLarge)
            .clickable(onClick = onClick)
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.label_add_new_exercises_home),
            style = MaterialTheme.typography.headlineLarge,
        )
        Icon(
            imageVector = KenkoIcons.AddLarge,
            contentDescription = null,
        )
    }
}

@Composable
private fun CurrentPlanCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .cardWidth()
            .aspectRatio(PHI)
            .clip(MaterialTheme.shapes.extraLarge)
            .border(OutlineBorder, MaterialTheme.shapes.extraLarge)
            .clickable(onClick = onClick)
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.label_current_plan_home),
            style = MaterialTheme.typography.headlineLarge,
        )
        Icon(
            modifier = Modifier.offset(40.dp),
            imageVector = KenkoIcons.Colony,
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun HomePreview() {
    KenkoTheme {
        Home(
            state = HomeUiData(
                isPlanSelected = true,
                isSessionStarted = true,
                hasMultipleSessions = true,
                currentPlanId = null,
            ),
        )
    }
}

@Preview
@Composable
private fun FirstStartHomePreview() {
    KenkoTheme {
        Home(
            state = HomeUiData(
                isPlanSelected = false,
                isSessionStarted = false,
                hasMultipleSessions = false,
                currentPlanId = null,
            ),
        )
    }
}
