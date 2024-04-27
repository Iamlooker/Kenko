package com.looker.kenko.ui.addSet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Done
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.looker.kenko.R
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.ui.components.draggableTextField.DraggableTextField
import com.looker.kenko.ui.components.draggableTextField.rememberDraggableTextFieldState
import com.looker.kenko.ui.theme.KenkoTheme

private val incrementButtonModifier = Modifier
    .height(48.dp)
    .zIndex(0f)

private val zIndexModifier = Modifier.zIndex(1F)

@Composable
fun AddSet(exercise: Exercise, onDone: () -> Unit) {
    LaunchedEffect(exercise) {

    }
    val viewModel: AddSetViewModel =
        hiltViewModel<AddSetViewModel, AddSetViewModel.AddSetViewModelFactory>(key = exercise.name) {
            it.create(exercise.name)
        }
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .wrapContentHeight()
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        AddSetHeader(
            modifier = Modifier.fillMaxWidth(),
            exerciseName = exercise.name,
            onClick = {
                viewModel.addSet()
                onDone()
            },
        )

        Spacer(modifier = Modifier.height(24.dp))

        SwipeableTextField(
            modifier = Modifier.align(CenterHorizontally)
        ) {
            TextButton(
                modifier = incrementButtonModifier,
                onClick = { viewModel.addRep(-1) }
            ) {
                Text(text = "-1")
            }
            val reps = rememberDraggableTextFieldState(
                supportingText = stringResource(R.string.label_reps).uppercase(),
                textFieldState = viewModel.reps,
                events = viewModel.repsDragEvents,
            )
            DraggableTextField(dragState = reps, modifier = zIndexModifier)
            TextButton(
                modifier = incrementButtonModifier,
                onClick = { viewModel.addRep(1) }
            ) {
                Text(text = "+1")
            }
            TextButton(
                modifier = incrementButtonModifier,
                onClick = { viewModel.addRep(5) }
            ) {
                Text(text = "+5")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        SwipeableTextField(
            modifier = Modifier.align(CenterHorizontally)
        ) {
            TextButton(
                modifier = incrementButtonModifier,
                onClick = { viewModel.addWeight(-1.0) }
            ) {
                Text(text = "-1.0")
            }
            val weights = rememberDraggableTextFieldState(
                supportingText = stringResource(R.string.label_weight).uppercase(),
                textFieldState = viewModel.weights,
                events = viewModel.weightsDragEvents,
            )
            DraggableTextField(dragState = weights, modifier = zIndexModifier)
            TextButton(
                modifier = incrementButtonModifier,
                onClick = { viewModel.addWeight(1.0) }
            ) {
                Text(text = "+1.0")
            }
            TextButton(
                modifier = incrementButtonModifier,
                onClick = { viewModel.addWeight(5.0) }
            ) {
                Text(text = "+5.0")
            }
        }
        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Composable
private fun AddSetHeader(
    exerciseName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1F)) {
            Text(
                text = stringResource(R.string.label_add_set_for).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = exerciseName,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        FilledTonalIconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.TwoTone.Done,
                contentDescription = ""
            )
        }
    }
}

@Composable
private fun SwipeableTextField(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        modifier = modifier.requiredHeight(48.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            content()
        }
    }
}

@Preview
@Composable
private fun UpdateTextFieldPreview() {
    KenkoTheme {
        SwipeableTextField {

        }
    }
}
