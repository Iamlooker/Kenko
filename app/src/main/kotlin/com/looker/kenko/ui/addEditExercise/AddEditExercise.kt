package com.looker.kenko.ui.addEditExercise

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.ui.components.BackButton
import com.looker.kenko.ui.components.ErrorSnackbar
import com.looker.kenko.ui.components.FlowHorizontalChips
import com.looker.kenko.ui.components.KenkoButton
import com.looker.kenko.ui.components.kenkoTextFieldColor
import com.looker.kenko.ui.extensions.plus
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun AddEditExercise(
    onDone: () -> Unit,
    onBackPress: () -> Unit,
) {
    val viewModel: AddEditExerciseViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AddEditExercise(
        exerciseName = viewModel.exerciseName,
        exerciseReference = viewModel.reference,
        state = state,
        snackbarState = viewModel.snackbarState,
        onSelectTarget = viewModel::setTargetMuscle,
        onSelectIsometric = viewModel::setIsometric,
        onNameChange = viewModel::setName,
        onReferenceChange = viewModel::addReference,
        onBackPress = onBackPress,
        onDone = { viewModel.addNewExercise(onDone) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditExercise(
    exerciseName: String,
    exerciseReference: String,
    state: AddEditExerciseUiState,
    snackbarState: SnackbarHostState,
    onSelectTarget: (MuscleGroups) -> Unit,
    onSelectIsometric: (Boolean) -> Unit,
    onNameChange: (String) -> Unit,
    onReferenceChange: (String) -> Unit,
    onDone: () -> Unit,
    onBackPress: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackButton(onBackPress)
                },
                title = {
                    Text(
                        text = stringResource(
                            if (exerciseName.isBlank()) {
                                R.string.label_new_exercise
                            } else {
                                R.string.label_edit_exercise
                            }
                        ),
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) {
                ErrorSnackbar(data = it)
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(PaddingValues(horizontal = 16.dp) + innerPadding),
        ) {
            ExerciseTextField(
                exerciseName = exerciseName,
                onNameChange = onNameChange,
                isError = state.isError,
                isReadOnly = state.isReadOnly,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.label_target),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outline
            )
            FlowHorizontalChips(target = state.targetMuscle, onSet = onSelectTarget)
            Spacer(modifier = Modifier.height(12.dp))
            IsIsometricButton(isIsometric = state.isIsometric, onChange = onSelectIsometric)
            Spacer(modifier = Modifier.height(18.dp))
            ReferenceTextField(
                reference = exerciseReference,
                onReferenceChange = onReferenceChange,
                isError = state.isReferenceInvalid,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(18.dp))
            KenkoButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .navigationBarsPadding(),
                onClick = onDone,
                label = {
                    Icon(
                        imageVector = KenkoIcons.Save,
                        contentDescription = null,
                    )
                },
                icon = {
                    Text(stringResource(R.string.label_save))
                }
            )
            Spacer(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
private fun ReferenceTextField(
    reference: String,
    isError: Boolean,
    onReferenceChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        modifier = modifier,
        value = reference,
        onValueChange = onReferenceChange,
        colors = kenkoTextFieldColor(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
        shape = MaterialTheme.shapes.large,
        supportingText = {
            Text(text = stringResource(R.string.label_reference_optional))
        },
        label = {
            Text(text = stringResource(R.string.label_reference))
        },
        isError = isError,
        leadingIcon = {
            Icon(imageVector = KenkoIcons.Lightbulb, contentDescription = null)
        }
    )
}

@Composable
private fun ExerciseTextField(
    exerciseName: String,
    isError: Boolean,
    isReadOnly: Boolean,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        modifier = modifier,
        value = exerciseName,
        onValueChange = onNameChange,
        colors = kenkoTextFieldColor(),
        readOnly = isReadOnly,
        shape = MaterialTheme.shapes.large,
        label = {
            Text(text = stringResource(R.string.label_name))
        },
        isError = isError,
        leadingIcon = {
            Icon(imageVector = KenkoIcons.Rename, contentDescription = null)
        },
        supportingText = {
            if (isError) {
                Text(text = stringResource(R.string.label_exercise_exists))
            }
        }
    )
}

@Composable
private fun IsIsometricButton(isIsometric: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onChange(!isIsometric) }
    ) {
        Column(modifier = Modifier.weight(1F)) {
            Text(
                text = stringResource(R.string.label_is_isometric),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = stringResource(R.string.label_is_isometric_DESC),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
            )
        }
        Switch(checked = isIsometric, onCheckedChange = onChange)
    }
}

@PreviewLightDark
@Composable
private fun ReferenceTextFieldPreview() {
    KenkoTheme {
        ReferenceTextField(
            reference = "https://youtu.be",
            onReferenceChange = {},
            isError = false,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IsIsometricButtonPreview() {
    KenkoTheme {
        var isIso by remember {
            mutableStateOf(false)
        }
        IsIsometricButton(isIsometric = isIso, onChange = { isIso = !isIso })
    }
}

@Preview(name = "Exercise Name Field")
@Composable
private fun NameTextFieldPreview() {
    KenkoTheme {
        ExerciseTextField(
            exerciseName = "Bench Press",
            onNameChange = {},
            isError = false,
            isReadOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "Exercise Name Field - Error")
@Composable
private fun ErrorNameTextFieldPreview() {
    KenkoTheme {
        ExerciseTextField(
            exerciseName = "Bench Press",
            onNameChange = {},
            isError = true,
            isReadOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddEditPreview() {
    KenkoTheme {
        AddEditExercise(
            exerciseName = "BenchPress",
            exerciseReference = "yt.be",
            state = AddEditExerciseUiState(MuscleGroups.Chest, false, false, false, false),
            snackbarState = SnackbarHostState(),
            onSelectTarget = {},
            onSelectIsometric = {},
            onNameChange = {},
            onReferenceChange = {},
            onDone = {},
            onBackPress = {}
        )
    }
}
