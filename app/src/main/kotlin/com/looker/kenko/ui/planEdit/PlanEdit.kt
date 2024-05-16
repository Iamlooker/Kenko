package com.looker.kenko.ui.planEdit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.data.model.sampleExercises
import com.looker.kenko.ui.components.BackButton
import com.looker.kenko.ui.components.ErrorSnackbar
import com.looker.kenko.ui.components.kenkoTextFieldColor
import com.looker.kenko.ui.helper.normalizeInt
import com.looker.kenko.ui.helper.vertical
import com.looker.kenko.ui.planEdit.components.DayItem
import com.looker.kenko.ui.planEdit.components.DaySelector
import com.looker.kenko.ui.planEdit.components.ExerciseItem
import com.looker.kenko.ui.planEdit.components.SelectExerciseButton
import com.looker.kenko.ui.planEdit.components.dayName
import com.looker.kenko.ui.selectExercise.SelectExercise
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek

@Composable
fun PlanEdit(
    viewModel: PlanEditViewModel,
    onBackPress: () -> Unit,
    onAddNewExerciseClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PlanEdit(
        snackbarHostState = viewModel.snackbarState,
        state = state,
        planName = viewModel.planName,
        onSelectDay = viewModel::setCurrentDay,
        onNameChange = viewModel::setName,
        onAddExercisesClick = viewModel::openSheet,
        onRemoveExerciseClick = viewModel::removeExercise,
        onSaveClick = viewModel::savePlan,
        onBackPress = onBackPress,
    )

    if (state.isSheetVisible) {
        AddExerciseSheet(
            onDismiss = viewModel::closeSheet,
            onDone = viewModel::addExercise,
            onAddNewExerciseClick = onAddNewExerciseClick
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlanEdit(
    snackbarHostState: SnackbarHostState,
    state: PlanEditUiData,
    planName: String,
    onSelectDay: (DayOfWeek) -> Unit,
    onNameChange: (String) -> Unit,
    onAddExercisesClick: () -> Unit,
    onRemoveExerciseClick: (Exercise) -> Unit,
    onSaveClick: (() -> Unit) -> Unit,
    onBackPress: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val isCurrentDayBlank by remember { derivedStateOf { state.exercises.isEmpty() } }
    Scaffold(
        floatingActionButton = {
            SelectExerciseButton(
                onClick = {
                    focusManager.clearFocus()
                    onAddExercisesClick()
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                ErrorSnackbar(data = it)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Icon(
                modifier = Modifier.align(Alignment.TopEnd),
                imageVector = KenkoIcons.Wireframe,
                tint = MaterialTheme.colorScheme.outline,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-10).dp, y = 30.dp)
                    .vertical(false),
                text = dayName(dayOfWeek = state.currentDay),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.surfaceContainer,
            )
        }
        LazyColumn(
            contentPadding = innerPadding,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            stickyHeader {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        BackButton(onClick = onBackPress)
                        OutlinedButton(
                            onClick = { onSaveClick(onBackPress) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                        ) {
                            Text(text = stringResource(R.string.label_save))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        value = planName,
                        shape = MaterialTheme.shapes.medium,
                        onValueChange = onNameChange,
                        colors = kenkoTextFieldColor(),
                        label = {
                            Text(text = stringResource(R.string.label_name))
                        },
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DaySelector(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        dayItem = {
                            DayItem(
                                dayOfWeek = it,
                                isSelected = it == state.currentDay,
                                onClick = {
                                    focusManager.clearFocus()
                                    onSelectDay(it)
                                },
                            )
                        }
                    )
                }
            }
            if (isCurrentDayBlank) {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_exercises_yet),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            } else {
                itemsIndexed(state.exercises) { index, exercise ->
                    ExerciseItem(exercise = exercise) {
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
        }
    }
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
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Icon(imageVector = KenkoIcons.Remove, contentDescription = null)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = normalizeInt(index + 1))
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
    val state = rememberModalBottomSheetState()
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
private fun ExerciseItemPreview() {
    KenkoTheme {
        ExerciseItem(exercise = MuscleGroups.Chest.sampleExercises.first()) {
            ExerciseItemActions(index = 1) {

            }
        }
    }
}
