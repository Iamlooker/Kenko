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

package com.looker.kenko.ui.sessions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.model.Session
import com.looker.kenko.ui.components.BackButton
import com.looker.kenko.ui.components.TertiaryKenkoButton
import com.looker.kenko.ui.extensions.plus
import com.looker.kenko.ui.planEdit.components.dayName
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.utils.DateTimeFormat
import com.looker.kenko.utils.formatDate
import com.looker.kenko.utils.isToday
import kotlinx.datetime.LocalDate

@Composable
fun Sessions(
    viewModel: SessionsViewModel,
    onSessionClick: (LocalDate?) -> Unit,
    onBackPress: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Sessions(
        state = state,
        onSessionClick = onSessionClick,
        onBackPress = onBackPress,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Sessions(
    state: SessionsUiData,
    onSessionClick: (LocalDate?) -> Unit,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackButton(onClick = onBackPress)
                },
                title = {
                    Text(text = stringResource(id = R.string.label_session))
                },
            )
        },
        floatingActionButton = {
            TertiaryKenkoButton(
                onClick = { onSessionClick(null) },
                label = {
                    val isCurrentSessionActive = state.isCurrentSessionActive
                    val text = remember(isCurrentSessionActive) {
                        if (isCurrentSessionActive) {
                            R.string.label_continue_session
                        } else {
                            R.string.label_start_session
                        }
                    }
                    Text(text = stringResource(id = text))
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = KenkoIcons.ArrowOutward,
                        contentDescription = null,
                    )
                },
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding + PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            items(
                items = state.sessions,
                key = { it.id!! },
            ) { session ->
                SessionCard(
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 8.dp,
                    ),
                    session = session,
                    isTodayLabel = {
                        val isToday = remember(session.date) {
                            session.date.isToday
                        }
                        if (isToday) {
                            IsTodayLabel()
                        }
                    },
                    onClick = { onSessionClick(session.date) },
                )
            }
        }
    }
}

@Composable
private fun IsTodayLabel() {
    Surface(
        shape = CircleShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            text = stringResource(R.string.label_today),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}

@Composable
fun SessionCard(
    session: Session,
    modifier: Modifier = Modifier,
    isTodayLabel: @Composable () -> Unit,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.large,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp, vertical = 16.dp),
        ) {
            isTodayLabel()
            Text(
                text = dayName(session.date.dayOfWeek),
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = session.formattedDate(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

@Composable
fun Session.formattedDate(format: DateTimeFormat = DateTimeFormat.Short): String =
    remember(date) {
        formatDate(date, dateTimeFormat = format)
    }

@Preview
@Composable
private fun TodayLabelPreview() {
    KenkoTheme {
        IsTodayLabel()
    }
}

@PreviewLightDark
@Composable
private fun SessionCardPreview() {
    KenkoTheme {
        SessionCard(
            session = Session(
                planId = 1,
                date = LocalDate(2024, 4, 15),
                sets = emptyList(),
            ),
            isTodayLabel = { IsTodayLabel() },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun SessionsPreview() {
    KenkoTheme {
        Sessions(
            state = SessionsUiData(listOf(Session(1, emptyList())), false),
            onBackPress = {},
            onSessionClick = {},
        )
    }
}
