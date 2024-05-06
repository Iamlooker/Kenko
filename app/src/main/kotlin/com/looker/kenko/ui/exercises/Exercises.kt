package com.looker.kenko.ui.exercises

import androidx.compose.foundation.ExperimentalFoundationApi
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Exercises(onNavigateToExercise: (String?) -> Unit) {
    val viewModel: ExercisesViewModel = hiltViewModel()
    val state by viewModel.exercises.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        floatingActionButton = {
            AddExerciseButton(onClick = { onNavigateToExercise(null) })
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding + PaddingValues(bottom = 96.dp)
        ) {
            stickyHeader {
                MuscleGroupFilterChips(
                    target = state.selected,
                    onSelect = viewModel::setTarget
                )
            }
            items(state.exercises, key = { it.name }) { exercise ->
                ExerciseItem(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    exercise = exercise,
                    onClick = { onNavigateToExercise(exercise.name) },
                    onReferenceClick = { uriHandler.openUri(exercise.reference!!) }
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
            .horizontalScroll(rememberScrollState()),
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
    onReferenceClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
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
                        text = stringResource(exercise.target.stringRes),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
                Box(modifier = Modifier.size(56.dp)) {
                    if (exercise.reference != null) {
                        FilledTonalIconButton(
                            modifier = Modifier.size(56.dp),
                            shape = MaterialTheme.shapes.extraLarge,
                            onClick = onReferenceClick
                        ) {
                            Icon(imageVector = KenkoIcons.Lightbulb, contentDescription = null)
                        }
                    }
                }
            }
//            Text(
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .offset(y = 18.dp),
//                text = exercise.name,
//                style = MaterialTheme.typography.displaySmall,
//                color = MaterialTheme.colorScheme.outlineVariant
//            )
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
            onReferenceClick = {}
        )
    }
}
