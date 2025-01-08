package com.looker.kenko.ui.exercises

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.ExercisesPreviewParameter
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.ui.components.BackButton
import com.looker.kenko.ui.components.ErrorSnackbar
import com.looker.kenko.ui.components.HorizontalTargetChips
import com.looker.kenko.ui.components.KenkoBorderWidth
import com.looker.kenko.ui.components.SecondaryKenkoButton
import com.looker.kenko.ui.components.SwipeToDeleteBox
import com.looker.kenko.ui.extensions.plus
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun Exercises(
    viewModel: ExercisesViewModel,
    onExerciseClick: (id: Int?) -> Unit,
    onCreateClick: (target: MuscleGroups?) -> Unit,
    onBackPress: () -> Unit,
) {
    val state by viewModel.exercises.collectAsStateWithLifecycle()
    Exercises(
        state = state,
        snackbarState = viewModel.snackbarState,
        onBackPress = onBackPress,
        onExerciseClick = onExerciseClick,
        onCreateClick = onCreateClick,
        onSelectTarget = viewModel::setTarget,
        onReferenceClick = viewModel::onReferenceClick,
        onRemove = viewModel::removeExercise,
    )
}

@Composable
private fun Exercises(
    state: ExercisesUiState,
    snackbarState: SnackbarHostState,
    onExerciseClick: (id: Int?) -> Unit,
    onCreateClick: (target: MuscleGroups?) -> Unit,
    onSelectTarget: (MuscleGroups?) -> Unit,
    onRemove: (Int?) -> Unit,
    onBackPress: () -> Unit,
    onReferenceClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxWidth(),
        floatingActionButton = {
            SecondaryKenkoButton(
                onClick = { onCreateClick(state.selected) },
                label = {
                    Icon(
                        imageVector = KenkoIcons.Add,
                        contentDescription = null,
                    )
                },
                icon = {
                    Text(stringResource(R.string.label_create_exercise))
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) {
                ErrorSnackbar(data = it)
            }
        },
        topBar = {
            Header(
                target = state.selected,
                onSelect = onSelectTarget,
                onBackPress = onBackPress,
            )
        },
    ) { innerPadding ->
        ExercisesList(
            exercises = state.exercises,
            contentPadding = innerPadding + PaddingValues(bottom = 80.dp),
            onExerciseClick = onExerciseClick,
            onReferenceClick = onReferenceClick,
            onRemove = onRemove,
        )
    }
}

@Composable
private fun ExercisesList(
    exercises: List<Exercise>,
    contentPadding: PaddingValues,
    onExerciseClick: (id: Int?) -> Unit,
    onRemove: (Int?) -> Unit,
    onReferenceClick: (String) -> Unit,
) {
    LazyColumn(
        contentPadding = contentPadding,
    ) {
        items(exercises, key = { it.id!! }) { exercise ->
            val exerciseId by rememberUpdatedState(exercise.id)
            SwipeToDeleteBox(
                modifier = Modifier.animateItem(),
                onDismiss = { onRemove(exerciseId) }
            ) {
                ExerciseItem(
                    exercise = exercise,
                    onClick = { onExerciseClick(exerciseId) },
                    referenceButton = {
                        if (exercise.reference != null) {
                            FilledTonalIconButton(
                                modifier = Modifier.size(56.dp),
                                shape = MaterialTheme.shapes.extraLarge,
                                onClick = { onReferenceClick(exercise.reference) }
                            ) {
                                Icon(imageVector = KenkoIcons.Lightbulb, contentDescription = null)
                            }
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Header(
    target: MuscleGroups?,
    onSelect: (MuscleGroups?) -> Unit,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.label_browse_exercises))
            },
            navigationIcon = {
                BackButton(onClick = onBackPress)
            }
        )
        HorizontalTargetChips(
            target = target,
            onSelect = onSelect,
            contentPadding = PaddingValues(12.dp),
        )
        HorizontalDivider(thickness = KenkoBorderWidth)
    }
}

@Composable
private fun ExerciseItem(
    exercise: Exercise,
    onClick: () -> Unit,
    referenceButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    @StringRes
    val targetName: Int = remember { exercise.target.stringRes }
    Surface(
        modifier = modifier,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = stringResource(targetName),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
            Box(modifier = Modifier.size(56.dp)) {
                referenceButton()
            }
        }
    }
}

@Preview
@Composable
private fun ExercisesPreview(
    @PreviewParameter(ExercisesPreviewParameter::class, limit = 2) exercises: List<Exercise>
) {
    KenkoTheme {
        Exercises(
            state = ExercisesUiState(MuscleGroups.entries.flatMap { exercises }),
            snackbarState = SnackbarHostState(),
            onExerciseClick = {},
            onCreateClick = {},
            onSelectTarget = {},
            onBackPress = {},
            onReferenceClick = {},
            onRemove = {}
        )
    }
}
