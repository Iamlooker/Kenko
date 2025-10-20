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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.ui.components.HorizontalTargetChips
import com.looker.kenko.ui.components.disableScrollConnection
import com.looker.kenko.ui.components.kenkoTextFieldColor
import com.looker.kenko.ui.planEdit.components.ExerciseItem
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.end
import com.looker.kenko.ui.theme.start

@Composable
fun SelectExercise(
    onDone: (Exercise) -> Unit,
    onRequestNewExercise: (name: String?, target: MuscleGroups?) -> Unit,
) {
    val viewModel: SelectExerciseViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .nestedScroll(disableScrollConnection())
            .wrapContentHeight(),
    ) {
        val target by viewModel.targetMuscle.collectAsStateWithLifecycle()
        val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()

        AddExerciseHeader(modifier = Modifier.padding(horizontal = 16.dp))
        ExerciseSearchField(
            modifier = Modifier.padding(horizontal = 16.dp),
            name = viewModel.searchQuery,
            onNameChange = viewModel::setSearch,
            onAddClick = {
                onRequestNewExercise(viewModel.searchQuery.ifBlank { null }, target)
            },
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
                SearchNotFound(
                    onAddNewExercise = {
                        onRequestNewExercise(viewModel.searchQuery, target)
                    },
                )
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
                Icon(painter = KenkoIcons.Add, contentDescription = null)
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
            Icon(painter = KenkoIcons.Add, contentDescription = null)
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
