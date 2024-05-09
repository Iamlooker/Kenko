package com.looker.kenko.ui.exercises

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.data.model.sampleExercises
import com.looker.kenko.ui.helper.plus
import com.looker.kenko.ui.planEdit.components.AddExerciseButton
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun Exercises(onNavigateToExercise: (name: String?, target: MuscleGroups?) -> Unit) {
    val viewModel: ExercisesViewModel = hiltViewModel()
    val state by viewModel.exercises.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        floatingActionButton = {
            AddExerciseButton(onClick = { onNavigateToExercise(null, state.selected) })
        },
        floatingActionButtonPosition = FabPosition.Center,
        topBar = {
            MuscleGroupFilterChips(
                target = state.selected,
                onSelect = viewModel::setTarget
            )
        },
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding + PaddingValues(bottom = 96.dp)
        ) {
            items(state.exercises, key = { it.name }) { exercise ->
                val hasReference = remember {
                    exercise.reference != null
                }
                ExerciseItem(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    exercise = exercise,
                    onClick = { onNavigateToExercise(exercise.name, null) },
                    referenceButton = {
                        if (hasReference) {
                            FilledTonalIconButton(
                                modifier = Modifier.size(56.dp),
                                shape = MaterialTheme.shapes.extraLarge,
                                onClick = { uriHandler.openUri(exercise.reference!!) }
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

@Composable
private fun MuscleGroupFilterChips(
    target: MuscleGroups?,
    onSelect: (MuscleGroups?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(top = 16.dp, bottom = 12.dp)
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val targets = remember { listOf(null) + MuscleGroups.entries }
        Spacer(modifier = Modifier.width(12.dp))
        targets.forEach { muscle ->
            FilterChip(
                selected = target == muscle,
                onClick = { onSelect(muscle) },
                label = { Text(text = stringResource(muscle.string)) }
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
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
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = stringResource(targetName),
                    style = MaterialTheme.typography.titleMedium,
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
private fun ExerciseItemPreview() {
    KenkoTheme {
        ExerciseItem(
            exercise = MuscleGroups.Chest.sampleExercises.first(),
            onClick = {},
            referenceButton = {}
        )
    }
}
