package com.looker.kenko.ui.selectExercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.looker.kenko.ui.components.HorizontalTargetChips
import com.looker.kenko.ui.components.kenkoTextFieldColor
import com.looker.kenko.ui.planEdit.components.ExerciseItem
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.end
import com.looker.kenko.ui.theme.start

@Composable
fun SelectExercise(
    onDone: (Exercise) -> Unit,
    onRequestNewExercise: () -> Unit,
) {
    val viewModel: SelectExerciseViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .wrapContentHeight(),
    ) {
        val target by viewModel.targetMuscle.collectAsStateWithLifecycle()
        val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()

        AddExerciseHeader(modifier = Modifier.padding(horizontal = 16.dp))
        ExerciseSearchField(
            modifier = Modifier.padding(horizontal = 16.dp),
            name = viewModel.searchQuery,
            onNameChange = viewModel::setSearch,
            onAddClick = onRequestNewExercise,
        )
        HorizontalTargetChips(
            target = target,
            onSelect = viewModel::setTarget,
            contentPadding = PaddingValues(horizontal = 16.dp),
        )

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
    LazyColumn(Modifier.height(240.dp)) {
        items(searchResult.exercises) { exercise ->
            ExerciseItem(
                exercise = exercise,
                onClick = { onClick(exercise) },
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
                text = stringResource(R.string.error_cant_find_exercise),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onErrorContainer,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onAddNewExercise,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onErrorContainer,
                    contentColor = MaterialTheme.colorScheme.errorContainer
                ),
            ) {
                Icon(imageVector = KenkoIcons.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = stringResource(R.string.label_create_exercise))
            }
        }
    }
}

@Composable
private fun ExerciseSearchField(
    name: String,
    onNameChange: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            modifier = Modifier.weight(1f),
            value = name,
            onValueChange = onNameChange,
            colors = kenkoTextFieldColor(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            shape = MaterialTheme.shapes.large.end(8.dp),
            label = {
                Text(text = stringResource(R.string.label_search_exercise))
            },
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilledTonalIconButton(
            modifier = Modifier.size(56.dp),
            shape = MaterialTheme.shapes.large.start(8.dp),
            onClick = onAddClick,
        ) {
            Icon(imageVector = KenkoIcons.Add, contentDescription = null)
        }
    }
}

@Composable
private fun AddExerciseHeader(
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.label_add_exercise_header),
        style = MaterialTheme.typography.displayMedium,
        color = MaterialTheme.colorScheme.tertiary
    )
}

@Preview
@Composable
private fun ErrorPreview() {
    KenkoTheme {
        SearchNotFound(onAddNewExercise = {})
    }
}
