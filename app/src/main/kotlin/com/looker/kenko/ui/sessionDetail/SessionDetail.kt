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

package com.looker.kenko.ui.sessionDetail

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonShapes
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.localDate
import com.looker.kenko.ui.addSet.AddSet
import com.looker.kenko.ui.components.BackButton
import com.looker.kenko.ui.components.SwipeToDeleteBox
import com.looker.kenko.ui.components.TypingText
import com.looker.kenko.ui.extensions.normalizeInt
import com.looker.kenko.ui.extensions.plus
import com.looker.kenko.ui.planEdit.components.dayName
import com.looker.kenko.ui.sessionDetail.components.SetItem
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.utils.DateTimeFormat
import com.looker.kenko.utils.formatDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SessionDetails(
    viewModel: SessionDetailViewModel,
    onBackPress: () -> Unit,
    onHistoryClick: (LocalDate) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SessionDetail(
        state = state,
        onBackPress = onBackPress,
        onRemoveSet = viewModel::removeSet,
        onReferenceClick = viewModel::openReference,
        onSelectBottomSheet = viewModel::showBottomSheet,
        onHistoryClick = { onHistoryClick(viewModel.previousSessionDate)},
    )
    val exercise by viewModel.current.collectAsStateWithLifecycle()
    if (exercise != null) {
        AddSetSheet(
            exercise = exercise!!,
            onDismiss = viewModel::hideSheet,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionDetail(

    state: SessionDetailState,
    onBackPress: () -> Unit = {},
    onRemoveSet: (Int?) -> Unit = {},
    onReferenceClick: (String) -> Unit = {},
    onSelectBottomSheet: (Exercise) -> Unit = {},
    onHistoryClick: () -> Unit = {},
) {
    when (state) {
        is SessionDetailState.Error -> {
            Column(Modifier.statusBarsPadding()) {
                TopAppBar(
                    navigationIcon = {
                        BackButton(onClick = onBackPress)
                    },
                    title = {},
                )
                SessionError(
                    title = stringResource(state.title),
                    message = stringResource(state.errorMessage),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        SessionDetailState.Loading -> {
            Column(Modifier.statusBarsPadding()) {
                TopAppBar(
                    navigationIcon = {
                        BackButton(onClick = onBackPress)
                    },
                    title = {},
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        is SessionDetailState.Success -> {
            val data = state.data
            SetsList(
                date = data.date,
                exerciseSets = data.sets,
                isEditable = data.isToday,
                hasPreviousSession = data.hasPreviousSession,
                onBackPress = onBackPress,
                onRemoveSet = onRemoveSet,
                onReferenceClick = onReferenceClick,
                onSelectBottomSheet = onSelectBottomSheet,
                onHistoryClick = onHistoryClick,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SetsList(
    date: LocalDate,
    exerciseSets: Map<Exercise, List<Set>>,
    isEditable: Boolean,
    hasPreviousSession: Boolean,
    onBackPress: () -> Unit,
    onRemoveSet: (Int?) -> Unit,
    onReferenceClick: (String) -> Unit,
    onSelectBottomSheet: (Exercise) -> Unit,
    onHistoryClick: () -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(360.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        contentPadding = WindowInsets.navigationBars.asPaddingValues(LocalDensity.current) +
            PaddingValues(bottom = 12.dp),
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) },
        ) {
            Header(
                performedOn = date,
                onBackPress = onBackPress,
                actions = {
                    if (hasPreviousSession) {
                        IconButton(onClick = onHistoryClick) {
                            Icon(
                                painter = KenkoIcons.History,
                                contentDescription = null,
                            )
                        }
                    }
                },
            )
        }
        exerciseSets.forEach { (exercise, sets) ->
            item(
                span = { GridItemSpan(maxLineSpan) },
            ) {
                StickyHeader(name = exercise.name) {
                    if (!exercise.reference.isNullOrBlank()) {
                        FilledTonalIconButton(onClick = { onReferenceClick(exercise.reference) }) {
                            Icon(painter = KenkoIcons.Lightbulb, contentDescription = null)
                        }
                    }
                    if (isEditable) {
                        FilledTonalIconButton(
                            shapes = IconButtonShapes(
                                shape = MaterialShapes.Circle.toShape(),
                                pressedShape = MaterialShapes.Cookie6Sided.toShape(),
                            ),
                            onClick = { onSelectBottomSheet(exercise) },
                        ) {
                            Icon(painter = KenkoIcons.Add, contentDescription = null)
                        }
                    }
                }
            }
            itemsIndexed(items = sets) { index, set ->
                SwipeToDeleteBox(
                    modifier = Modifier.animateItem(),
                    onDismiss = { onRemoveSet(set.id) },
                ) {
                    SetItem(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        set = set,
                        title = {
                            Text(normalizeInt(index + 1))
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Header(
    performedOn: LocalDate,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit,
) {
    val date = remember {
        formatDate(performedOn, DateTimeFormat.Short)
    }
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            BackButton(onClick = onBackPress)
        },
        title = {
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                var startAnimatingDate by remember {
                    mutableStateOf(false)
                }
                TypingText(
                    text = dayName(performedOn.dayOfWeek),
                    onCompleteListener = {
                        startAnimatingDate = true
                    },
                )
                TypingText(
                    text = date,
                    startTyping = startAnimatingDate,
                    initialDelay = 0.milliseconds,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        },
        actions = {
            actions()
            // TODO: refactor to use isToday if possible
            if (performedOn == localDate) { //shows rest timer only on current workout
                TimerBox()
            }
        }

    )
}

@Composable
fun TimerBox() {
    val context = LocalContext.current.applicationContext
    val restTimerManager = remember { RestTimerManager(context) }
    var restTimerInSeconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true)
        {
            restTimerInSeconds = restTimerManager.updateTimer()
            delay(1000)
        }
    }

    Box(
        modifier = Modifier
            .padding(end = 16.dp)
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = formatTime(restTimerInSeconds),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, secs)
}

@Composable
private fun StickyHeader(
    name: String,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(24.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.weight(1F))
            if (actions != null) {
                actions()
            }
        }
    }
}

@Composable
private fun SessionError(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddSetSheet(
    exercise: Exercise,
    onDismiss: () -> Unit,
) {

    val context = LocalContext.current.applicationContext
    val restTimerManager = remember { RestTimerManager(context) }
    val scope = rememberCoroutineScope()
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(sheetState = state, onDismissRequest = onDismiss) {
        AddSet(
            exercise = exercise,
            onDone = {
                scope.launch { state.hide() }.invokeOnCompletion {
                    if (!state.isVisible) onDismiss()
                }
                restTimerManager.resetTimer()
            },
        )
    }
}

@PreviewLightDark
@Composable
private fun SessionDetailPreview() {
    KenkoTheme {
        val data = remember {
            SessionDetailState.Success(
                SessionUiData(
                    date = LocalDate(2024, 4, 15),
                    sets = emptyMap(),
                    isToday = true,
                ),
            )
        }
        Surface(modifier = Modifier.fillMaxSize()) {
            SessionDetail(state = data)
        }
    }
}

@PreviewLightDark
@Composable
private fun SessionErrorPreview() {
    KenkoTheme {
        val data = remember {
            SessionDetailState.Error.InvalidSession
        }
        Surface(modifier = Modifier.fillMaxSize()) {
            SessionDetail(state = data)
        }
    }
}
