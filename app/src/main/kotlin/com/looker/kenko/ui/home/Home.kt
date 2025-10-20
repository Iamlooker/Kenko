/*
 * Copyright (C) 2025 LooKeR & Contributors
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

package com.looker.kenko.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.ui.components.KenkoBorderWidth
import com.looker.kenko.ui.components.LiftingQuotes
import com.looker.kenko.ui.components.TertiaryKenkoButton
import com.looker.kenko.ui.components.TickerText
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.header

@Composable
fun Home(
    viewModel: HomeViewModel,
    onProfileClick: () -> Unit,
    onSelectPlanClick: () -> Unit,
    onAddExerciseClick: () -> Unit,
    onExploreSessionsClick: () -> Unit,
    onExploreExercisesClick: () -> Unit,
    onStartSessionClick: () -> Unit,
    onCurrentPlanClick: (Int) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Home(
        state = state,
        onProfileClick = onProfileClick,
        onSelectPlanClick = onSelectPlanClick,
        onAddExerciseClick = onAddExerciseClick,
        onExploreSessionsClick = onExploreSessionsClick,
        onExploreExercisesClick = onExploreExercisesClick,
        onStartSessionClick = onStartSessionClick,
        onCurrentPlanClick = onCurrentPlanClick,
    )
}

// TODO: Add current plan indicator on this page
@Composable
private fun Home(
    state: HomeUiData,
    onProfileClick: () -> Unit = {},
    onSelectPlanClick: () -> Unit = {},
    onAddExerciseClick: () -> Unit = {},
    onExploreSessionsClick: () -> Unit = {},
    onExploreExercisesClick: () -> Unit = {},
    onStartSessionClick: () -> Unit = {},
    onCurrentPlanClick: (Int) -> Unit = {},
) {
    Scaffold(
        topBar = {
            KenkoTopBar {
                IconButton(onClick = onProfileClick) {
                    Icon(painter = KenkoIcons.Circle, contentDescription = null)
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding),
        ) {
            HorizontalDivider(thickness = KenkoBorderWidth)
            AnimatedContent(
                modifier = Modifier.align(CenterHorizontally),
                targetState = state.isPlanSelected,
                label = "",
            ) { isPlanActive ->
                if (isPlanActive) {
                    Row(
                        modifier = Modifier
                            .widthIn(240.dp, 420.dp)
                            .height(120.dp),
                    ) {
                        ExploreExerciseCard(
                            onClick = onExploreExercisesClick,
                            onLongClick = onAddExerciseClick,
                            modifier = Modifier.weight(1F),
                        )
                        VerticalDivider()
                        SessionHistoryCard(
                            onClick = onExploreSessionsClick,
                            modifier = Modifier.weight(1F),
                        )
                    }
                } else {
                    TickerText(
                        text = stringResource(R.string.label_select_a_plan),
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
            HorizontalDivider(thickness = KenkoBorderWidth)
            if (state.isPlanSelected) {
                StartSession(
                    onStartSessionClick = {
                        if (state.isTodayEmpty) {
                            onCurrentPlanClick(state.currentPlanId!!)
                        } else {
                            onStartSessionClick()
                        }
                    },
                    content = {
                        val heading = remember(state.isSessionStarted, state.isTodayEmpty) {
                            if (state.isTodayEmpty) {
                                R.string.label_nothing_today
                            } else if (state.isSessionStarted) {
                                R.string.label_continue_session_heading
                            } else {
                                if (state.isFirstSession) {
                                    R.string.label_start_first_session
                                } else {
                                    R.string.label_start_session_heading
                                }
                            }
                        }
                        Text(
                            modifier = Modifier
                                .align(CenterHorizontally)
                                .padding(horizontal = 16.dp),
                            text = stringResource(heading),
                            style = MaterialTheme.typography.header()
                                .merge(
                                    lineBreak = LineBreak.Heading,
                                    color = MaterialTheme.colorScheme.primary,
                                ),
                        )
                    },
                    buttonText = {
                        val stringRes = remember(state.isSessionStarted, state.isTodayEmpty) {
                            if (state.isTodayEmpty) {
                                R.string.label_edit_plan
                            } else if (state.isSessionStarted) {
                                R.string.label_continue_session
                            } else {
                                R.string.label_start_session
                            }
                        }
                        Text(text = stringResource(stringRes))
                    },
                )
            } else {
                SelectPlan(onSelectPlanClick = onSelectPlanClick)
            }
            LiftingQuotes(Modifier.align(CenterHorizontally))
        }
    }
}

@Composable
private fun ColumnScope.StartSession(
    onStartSessionClick: () -> Unit,
    content: @Composable () -> Unit,
    buttonText: @Composable () -> Unit,
) {
    Spacer(modifier = Modifier.weight(1F))
    content()
    Spacer(modifier = Modifier.weight(1F))
    TertiaryKenkoButton(
        modifier = Modifier.align(CenterHorizontally),
        onClick = onStartSessionClick,
        label = buttonText,
        icon = {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = KenkoIcons.ArrowOutward,
                contentDescription = null,
            )
        },
    )
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
        text = stringResource(R.string.label_selecting_a_plan),
        style = MaterialTheme.typography.header().copy(
            lineBreak = LineBreak.Heading,
        ),
        color = MaterialTheme.colorScheme.primary,
    )
    Spacer(modifier = Modifier.weight(1F))
    Button(
        modifier = Modifier.align(CenterHorizontally),
        onClick = onSelectPlanClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
        ),
        contentPadding = PaddingValues(
            vertical = 24.dp,
            horizontal = 40.dp,
        ),
    ) {
        Text(text = stringResource(R.string.label_select_plan_one))
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            painter = KenkoIcons.ArrowOutward,
            contentDescription = null,
        )
    }
}

@Composable
private fun ExploreExerciseCard(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HelperCards(
        onClick = onClick,
        onLongClick = onLongClick,
        modifier = modifier,
    ) {
        Text(text = stringResource(R.string.label_explore_exercises))
        Icon(
            painter = KenkoIcons.ArrowOutward,
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .align(TopEnd),
        )
    }
}

@Composable
private fun SessionHistoryCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HelperCards(onClick = onClick, modifier = modifier) {
        Text(text = stringResource(R.string.label_session_history_home))
        Icon(
            painter = KenkoIcons.History,
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .align(TopEnd),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun KenkoTopBar(
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_app_icon),
                    contentDescription = null,
                    modifier = Modifier.clip(CircleShape)
                )
                Text(
                    text = "KENKO",
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        actions = actions,
        modifier = modifier,
    )
}

@Composable
private fun HelperCards(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: () -> Unit = {},
    shape: Shape = RectangleShape,
    color: Color = MaterialTheme.colorScheme.surface,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        shape = shape,
        color = color,
        modifier = modifier.combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick,
        ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            ProvideTextStyle(textStyle) { content() }
        }
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
                isTodayEmpty = false,
                isFirstSession = false,
                currentPlanId = null,
            ),
        )
    }
}

@Preview
@Composable
private fun StartTodayPreview() {
    KenkoTheme {
        Home(
            state = HomeUiData(
                isPlanSelected = true,
                isSessionStarted = false,
                isTodayEmpty = false,
                isFirstSession = false,
                currentPlanId = null,
            ),
        )
    }
}

@Preview
@Composable
private fun TodayEmptyPreview() {
    KenkoTheme {
        Home(
            state = HomeUiData(
                isPlanSelected = true,
                isSessionStarted = false,
                isTodayEmpty = true,
                isFirstSession = false,
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
                isTodayEmpty = false,
                isFirstSession = true,
                currentPlanId = null,
            ),
        )
    }
}
