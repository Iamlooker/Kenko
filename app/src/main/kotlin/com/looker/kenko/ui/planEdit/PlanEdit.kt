package com.looker.kenko.ui.planEdit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.data.model.sampleExercises
import com.looker.kenko.ui.addExercise.AddExercise
import com.looker.kenko.ui.components.BackButton
import com.looker.kenko.ui.components.kenkoTextFieldColor
import com.looker.kenko.ui.helper.normalizeInt
import com.looker.kenko.ui.planEdit.components.AddExerciseButton
import com.looker.kenko.ui.planEdit.components.DayItem
import com.looker.kenko.ui.planEdit.components.DaySelector
import com.looker.kenko.ui.planEdit.components.ExerciseItem
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import kotlinx.coroutines.launch

// TODO: Add Name field and save button
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlanEdit(
    onBackPress: () -> Unit,
    onRequestNewExercise: () -> Unit,
) {
    val viewModel: PlanEditViewModel = hiltViewModel()

    val currentDayOfWeek by viewModel.dayOfWeek.collectAsStateWithLifecycle()
    val isSheetVisible by viewModel.isSheetVisible.collectAsStateWithLifecycle()
    val currentExercises by viewModel.exercisesList.collectAsStateWithLifecycle()
    Scaffold(
        floatingActionButton = {
            AddExerciseButton(viewModel::openSheet)
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
        }
        LazyColumn(
            contentPadding = innerPadding,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                TextField(
                    value = viewModel.planName,
                    onValueChange = viewModel::setName,
                    colors = kenkoTextFieldColor(),
                    label = {
                        Text(text = stringResource(R.string.label_name))
                    },
                )
            }
            stickyHeader {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BackButton(
                        onClick = onBackPress,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    DaySelector(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        dayItem = {
                            DayItem(
                                dayOfWeek = it,
                                isSelected = it == currentDayOfWeek,
                                onClick = { viewModel.setCurrentDay(it) },
                            )
                        }
                    )
                }
            }
            itemsIndexed(currentExercises) { index, exercise ->
                ExerciseItem(exercise = exercise) {
                    ExerciseItemActions(
                        index = index,
                        onRemove = {
                            viewModel.removeExercise(exercise)
                        },
                    )
                }
            }
        }
    }

    if (isSheetVisible) {
        AddExerciseSheet(
            onDismiss = viewModel::closeSheet,
            onDone = viewModel::addExercise,
            onRequestNewExercise = onRequestNewExercise
        )
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
    onRequestNewExercise: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val state = rememberModalBottomSheetState()
    ModalBottomSheet(sheetState = state, onDismissRequest = onDismiss) {
        AddExercise(
            onDone = { exercise ->
                scope.launch {
                    onDone(exercise)
                    state.hide()
                }.invokeOnCompletion {
                    if (!state.isVisible) onDismiss()
                }
            },
            onRequestNewExercise = onRequestNewExercise,
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
