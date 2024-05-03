package com.looker.kenko.ui.addExercise

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.ui.components.kenkoTextFieldColor
import com.looker.kenko.ui.planEdit.components.ExerciseItem
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun AddExercise(onDone: (Exercise) -> Unit, onRequestNewExercise: () -> Unit) {
    val viewModel: AddExerciseViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val target by viewModel.targetMuscle.collectAsStateWithLifecycle()
        val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()

        AddExerciseHeader()
        ExerciseSearchField(name = viewModel.searchQuery, onNameChange = viewModel::setSearch)
        MuscleGroupFilterChips(target = target, onSelect = viewModel::setTarget)

        when (searchResult) {
            SearchResult.Loading -> {
                CircularProgressIndicator()
            }

            SearchResult.NotFound -> {
                SearchNotFound(onAddNewExercise = onRequestNewExercise)
            }

            is SearchResult.Success -> {
                SearchResult(
                    searchResult as SearchResult.Success,
                    onClick = onDone
                )
            }
        }
    }
}

@Composable
private fun SearchResult(
    searchResult: SearchResult.Success,
    onClick: (Exercise) -> Unit,
) {
    Column(
        modifier = Modifier
            .height(240.dp)
            .scrollable(rememberScrollState(), Orientation.Vertical)
    ) {
        searchResult.exercises.forEach { exercise ->
            ExerciseItem(
                exercise = exercise,
                modifier = Modifier.clickable { onClick(exercise) },
            ) {
                Spacer(modifier = Modifier)
            }
        }
    }
}

@Composable
private fun SearchNotFound(onAddNewExercise: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(MaterialTheme.colorScheme.errorContainer, MaterialTheme.shapes.large),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.label_cant_find_exercise),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onErrorContainer,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onAddNewExercise) {
                Text(text = stringResource(R.string.label_add_new_exercise))
            }
        }
    }
}

@Composable
private fun ExerciseSearchField(
    name: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        modifier = modifier,
        value = name,
        onValueChange = onNameChange,
        colors = kenkoTextFieldColor(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
        shape = MaterialTheme.shapes.large,
        label = {
            Text(text = stringResource(R.string.label_search_exercise))
        },
    )
}

@Composable
private fun AddExerciseHeader(
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.label_add_exercise),
        style = MaterialTheme.typography.displayMedium,
        color = MaterialTheme.colorScheme.tertiary
    )
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MuscleGroupFilterChips(
    target: MuscleGroups,
    onSelect: (MuscleGroups) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MuscleGroups.entries.forEach { muscle ->
            FilterChip(
                selected = target == muscle,
                onClick = { onSelect(muscle) },
                label = { Text(text = stringResource(muscle.stringRes)) }
            )
        }
    }
}

@Preview
@Composable
private fun ErrorPreview() {
    KenkoTheme {
        SearchNotFound(onAddNewExercise = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun MuscleFilterPreview() {
    KenkoTheme {
        MuscleGroupFilterChips(target = MuscleGroups.Chest) {

        }
    }
}
