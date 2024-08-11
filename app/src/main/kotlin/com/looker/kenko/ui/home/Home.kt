package com.looker.kenko.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.ui.components.KenkoBorderWidth
import com.looker.kenko.ui.components.LiftingQuotes
import com.looker.kenko.ui.components.icons.symbols.Add
import com.looker.kenko.ui.components.icons.symbols.ArrowOutward
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

// TODO: Add current plan indicator on this page
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
        ) {
            HorizontalDivider(thickness = KenkoBorderWidth)
            AnimatedContent(
                modifier = Modifier.align(CenterHorizontally),
                targetState = state.isPlanSelected, label = "",
            ) {
                if (it) {
                    Row(
                        modifier = Modifier
                            .widthIn(240.dp, 420.dp)
                            .height(120.dp),
                    ) {
                        ExploreExerciseCard(
                            modifier = Modifier
                                .weight(1F)
                                .clickable(onClick = onExploreExercisesClick),
                        )
                        VerticalDivider()
                        AddExerciseCard(
                            modifier = Modifier
                                .weight(1F)
                                .clickable(onClick = onAddExerciseClick),
                        )
                    }
                } else {
                    SelectPlanTicker()
                }
            }
            HorizontalDivider(thickness = KenkoBorderWidth)
            if (state.isPlanSelected) {
                StartSession(onStartSessionClick = onStartSessionClick) {
                    val stringRes = remember(state.isSessionStarted) {
                        if (state.isSessionStarted) {
                            R.string.label_continue_session
                        } else {
                            R.string.label_start_session
                        }
                    }
                    Text(text = stringResource(stringRes))
                }
            } else {
                SelectPlan(onSelectPlanClick = onSelectPlanClick)
            }
            LiftingQuotes(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .align(CenterHorizontally)
            )
        }
    }
}

@Composable
private fun ColumnScope.StartSession(
    onStartSessionClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Spacer(modifier = Modifier.weight(1F))
    FilledTonalButton(
        modifier = Modifier.align(CenterHorizontally),
        onClick = onStartSessionClick,
        contentPadding = PaddingValues(
            vertical = 24.dp,
            horizontal = 40.dp
        )
    ) {
        content()
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            modifier = Modifier.size(18.dp),
            imageVector = KenkoIcons.ArrowOutward,
            contentDescription = null
        )
    }
}

@Composable
private fun ColumnScope.SelectPlan(
    onSelectPlanClick: () -> Unit,
) {
    Spacer(modifier = Modifier.weight(1F))
    Text(
        modifier = Modifier
            .align(CenterHorizontally)
            .padding(horizontal = 16.dp),
        text = stringResource(R.string.label__selecting_a_plan),
        style = MaterialTheme.typography.displayLarge.copy(
            lineBreak = LineBreak.Heading
        ),
        color = MaterialTheme.colorScheme.tertiary,
    )
    Spacer(modifier = Modifier.weight(1F))
    FilledTonalButton(
        modifier = Modifier.align(CenterHorizontally),
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
            contentDescription = null
        )
    }
}

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
        Text(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .basicMarquee(initialDelayMillis = 0),
            text = tickerMarquee,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

@Composable
private fun ExploreExerciseCard(modifier: Modifier = Modifier) {
    HelperCards(modifier = modifier) {
        Text(text = stringResource(R.string.label_explore_exercises))
        Icon(
            modifier = Modifier
                .padding(16.dp)
                .align(TopEnd),
            imageVector = ArrowOutward,
            contentDescription = null
        )
    }
}

@Composable
private fun AddExerciseCard(modifier: Modifier = Modifier) {
    HelperCards(modifier = modifier) {
        Text(text = stringResource(R.string.label_add_new_exercises_home))
        Icon(
            modifier = Modifier
                .padding(16.dp)
                .align(TopEnd),
            imageVector = Add,
            contentDescription = null
        )
    }
}

@Composable
private fun HelperCards(
    modifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = MaterialTheme.colorScheme.surface,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
    ) {
        Box(
            modifier = boxModifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides textStyle
            ) {
                content()
            }
        }
    }
}

@PreviewScreenSizes
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

@PreviewScreenSizes
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
