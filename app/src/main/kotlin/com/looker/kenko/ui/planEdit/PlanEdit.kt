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

package com.looker.kenko.ui.planEdit

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.ExercisesPreviewParameter
import com.looker.kenko.ui.components.BackButton
import com.looker.kenko.ui.components.DaySelectorChip
import com.looker.kenko.ui.components.ErrorSnackbar
import com.looker.kenko.ui.components.HorizontalDaySelector
import com.looker.kenko.ui.components.KenkoButton
import com.looker.kenko.ui.extensions.normalizeInt
import com.looker.kenko.ui.extensions.plus
import com.looker.kenko.ui.planEdit.components.DaySwitcher
import com.looker.kenko.ui.planEdit.components.ExerciseItem
import com.looker.kenko.ui.planEdit.components.kenkoDayName
import com.looker.kenko.ui.selectExercise.SelectExercise
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.numbers
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek

@Composable
fun PlanEdit(
    viewModel: PlanEditViewModel,
    onBackPress: () -> Unit,
    onAddNewExerciseClick: () -> Unit,
) {
    val pageStage by viewModel.pageState.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    BackHandler {
        viewModel.onBackPress(pageStage, onBackPress)
    }
    FullEdit(
        snackbarHostState = viewModel.snackbarState,
        stage = pageStage,
        fab = {
            PlanEditFAB(
                pageStage = pageStage,
                onClick = {
                    if (pageStage == PlanEditStage.NameEdit) {
                        viewModel.saveName()
                    } else {
                        viewModel.openSheet()
                    }
                },
            )
        },
        onBackPress = { viewModel.onBackPress(pageStage, onBackPress) },
    ) { stage ->
        when (stage) {
            PlanEditStage.NameEdit -> {
                val isNameAlreadyUsed by viewModel.isNameAlreadyUsed.collectAsStateWithLifecycle()
                NameEdit(
                    state = viewModel.planNameState,
                    isNameAlreadyUsed = isNameAlreadyUsed,
                    onSaveClick = viewModel::saveName,
                )
            }

            PlanEditStage.PlanEdit -> {
                PlanEdit(
                    state = state,
                    onSelectDay = viewModel::setCurrentDay,
                    onRemoveExerciseClick = viewModel::removeExercise,
                    onFullDaySelection = viewModel::openFullDaySelection,
                )
            }
        }
    }

    if (state.exerciseSheetVisible) {
        AddExerciseSheet(
            onDismiss = viewModel::closeSheet,
            onDone = viewModel::addExercise,
            onAddNewExerciseClick = onAddNewExerciseClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FullEdit(
    snackbarHostState: SnackbarHostState,
    stage: PlanEditStage,
    fab: @Composable () -> Unit,
    onBackPress: () -> Unit,
    ui: @Composable (stage: PlanEditStage) -> Unit,
) {
    Scaffold(
        floatingActionButton = fab,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                ErrorSnackbar(data = it)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = { BackButton(onBackPress) },
            )
        },
    ) { innerPadding ->
        AnimatedContent(
            modifier = Modifier.padding(innerPadding + PaddingValues(horizontal = 16.dp)),
            targetState = stage,
            label = "Plan edit stage",
            transitionSpec = {
                when (targetState) {
                    PlanEditStage.NameEdit -> {
                        slideInHorizontally { -it / 2 } + fadeIn() togetherWith
                            slideOutHorizontally { it / 2 } + fadeOut()
                    }

                    PlanEditStage.PlanEdit -> {
                        slideInHorizontally { it / 2 } + fadeIn() togetherWith
                            slideOutHorizontally { -it / 2 } + fadeOut()
                    }
                } using SizeTransform(clip = false)
            },
        ) {
            ui(it)
        }
    }
}

@Composable
private fun PlanEditFAB(
    pageStage: PlanEditStage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    KenkoButton(
        modifier = modifier,
        onClick = onClick,
        label = {
            AnimatedContent(
                targetState = pageStage,
                label = "FAB label",
                transitionSpec = {
                    when (targetState) {
                        PlanEditStage.NameEdit -> {
                            slideInVertically { it } + fadeIn() togetherWith
                                slideOutVertically { -it } + fadeOut()
                        }

                        PlanEditStage.PlanEdit -> {
                            slideInVertically { -it } + fadeIn() togetherWith
                                slideOutVertically { it } + fadeOut()
                        }
                    } using SizeTransform(clip = false)
                },
            ) {
                if (it == PlanEditStage.NameEdit) {
                    Text(stringResource(R.string.label_next))
                } else {
                    Text(stringResource(R.string.label_add))
                }
            }
        },
        icon = {
            AnimatedContent(
                targetState = pageStage,
                label = "FAB icon",
                transitionSpec = {
                    when (targetState) {
                        PlanEditStage.NameEdit -> {
                            slideInHorizontally { it * 2 } + fadeIn() togetherWith
                                slideOutHorizontally { -it * 2 } + fadeOut()
                        }

                        PlanEditStage.PlanEdit -> {
                            slideInHorizontally { -it * 2 } + fadeIn() togetherWith
                                slideOutHorizontally { it * 2 } + fadeOut()
                        }
                    } using SizeTransform(clip = false)
                },
            ) {
                if (it == PlanEditStage.NameEdit) {
                    Icon(
                        painter = KenkoIcons.ArrowForward,
                        contentDescription = stringResource(R.string.label_next),
                    )
                } else {
                    Icon(
                        painter = KenkoIcons.Add,
                        contentDescription = stringResource(R.string.label_add),
                    )
                }
            }
        },
    )
}

@Composable
private fun NameEdit(
    state: TextFieldState,
    isNameAlreadyUsed: Boolean,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PlanName(
        planName = state,
        error = isNameAlreadyUsed,
        onNext = { onSaveClick() },
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
private fun PlanEdit(
    state: PlanEditState,
    onSelectDay: (DayOfWeek) -> Unit,
    onRemoveExerciseClick: (Exercise) -> Unit,
    onFullDaySelection: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val isCurrentDayBlank by remember(state.exercises) { derivedStateOf { state.exercises.isEmpty() } }
    PlanExercise(
        modifier = Modifier.fillMaxSize(),
        header = {
            Header(
                isExpandedView = state.selectionMode,
                daySelector = {
                    HorizontalDaySelector(
                        item = { dayOfWeek ->
                            DaySelectorChip(
                                selected = dayOfWeek == state.currentDay,
                                onClick = { onSelectDay(dayOfWeek) },
                            ) {
                                Text(kenkoDayName(dayOfWeek))
                            }
                        },
                    )
                },
                daySwitcher = {
                    DaySwitcher(
                        selected = state.currentDay,
                        onNext = { onSelectDay(state.currentDay + 1) },
                        onPrevious = { onSelectDay(state.currentDay - 1) },
                        onClick = onFullDaySelection,
                    )
                },
            )
        },
        items = {
            if (isCurrentDayBlank) {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.no_exercises_yet),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            } else {
                itemsIndexed(state.exercises) { index, exercise ->
                    ExerciseItem(
                        modifier = Modifier.animateItem(),
                        exercise = exercise,
                    ) {
                        ExerciseItemActions(
                            index = index,
                            onRemove = {
                                focusManager.clearFocus()
                                onRemoveExerciseClick(exercise)
                            },
                        )
                    }
                }
            }
        },
    )
}

@Composable
private fun ExerciseItemActions(
    index: Int,
    onRemove: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FilledTonalIconButton(
            onClick = onRemove,
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
            ),
        ) {
            Icon(painter = KenkoIcons.Remove, contentDescription = null)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = normalizeInt(index + 1),
            style = LocalTextStyle.current.numbers(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddExerciseSheet(
    onDismiss: () -> Unit,
    onDone: (Exercise) -> Unit,
    onAddNewExerciseClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(sheetState = state, onDismissRequest = onDismiss) {
        SelectExercise(
            onDone = { exercise ->
                scope.launch {
                    onDone(exercise)
                    state.hide()
                }.invokeOnCompletion {
                    if (!state.isVisible) onDismiss()
                }
            },
            onRequestNewExercise = onAddNewExerciseClick,
        )
    }
}

@Preview
@Composable
private fun ExerciseItemPreview(
    @PreviewParameter(ExercisesPreviewParameter::class, limit = 2) exercises: List<Exercise>,
) {
    KenkoTheme {
        ExerciseItem(exercise = exercises.first()) {
            ExerciseItemActions(index = 1) {
            }
        }
    }
}
