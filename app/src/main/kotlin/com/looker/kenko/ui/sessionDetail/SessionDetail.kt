package com.looker.kenko.ui.sessionDetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.Set
import com.looker.kenko.ui.addSet.AddSet
import com.looker.kenko.ui.components.BackButton
import com.looker.kenko.ui.components.SetItem
import com.looker.kenko.ui.components.texture.dottedGradient
import com.looker.kenko.ui.sessions.formattedDate
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.utils.DateTimeFormat
import kotlinx.coroutines.launch

@Composable
fun SessionDetails(onBackPress: () -> Unit) {
    val viewModel: SessionDetailViewModel = hiltViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()
    SessionDetail(
        state = state,
        onBackPress = onBackPress,
        onSelectBottomSheet = { viewModel.showBottomSheet(it) },
    )
    if (viewModel.isSheetExpanded) {
        AddSetSheet(
            exercise = viewModel.currentExercise,
            onAddSet = viewModel::addSet,
            onDismiss = viewModel::hideSheet
        )
    }
}

@Composable
private fun SessionDetail(
    state: SessionDetailState,
    onBackPress: () -> Unit,
    onSelectBottomSheet: (Exercise) -> Unit,
) {
    Column(
        modifier = Modifier
            .dottedGradient(MaterialTheme.colorScheme.tertiaryContainer)
            .statusBarsPadding()
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            BackButton(onClick = onBackPress)
        }
        when (state) {
            is SessionDetailState.Error -> {
                SessionError(
                    title = stringResource(state.title),
                    message = stringResource(state.errorMessage),
                    modifier = Modifier.fillMaxSize()
                )
            }

            SessionDetailState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SessionDetailState.Success -> {
                val data = state.data
                SessionList(
                    session = data.session,
                    exerciseSets = data.sets,
                    isEditable = data.isToday,
                    onSelectBottomSheet = onSelectBottomSheet,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SessionList(
    session: Session,
    exerciseSets: Map<Exercise, List<Set>>?,
    isEditable: Boolean,
    onSelectBottomSheet: (Exercise) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = WindowInsets.navigationBars.asPaddingValues(LocalDensity.current)
                .calculateBottomPadding() + 12.dp
        )
    ) {
        item {
            Header(
                performedOn = session.formattedDate(format = DateTimeFormat.Long),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        exerciseSets?.forEach { (exercise, sets) ->
            stickyHeader {
                StickyHeader(name = exercise.name) {
                    if (isEditable) {
                        FilledTonalIconButton(onClick = { onSelectBottomSheet(exercise) }) {
                            Icon(imageVector = Icons.TwoTone.Add, contentDescription = null)
                        }
                    }
                }
            }
            itemsIndexed(items = sets) { index, set ->
                val setTitle = remember(index) {
                    (index + 1).toString().padStart(2, '0')
                }
                SetItem(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = setTitle,
                    rep = set.repsOrDuration,
                    weight = set.weight
                )
            }
        }
    }
}

@Composable
private fun Header(
    performedOn: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = stringResource(R.string.label_performed_on).uppercase(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = performedOn,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
private fun StickyHeader(
    name: String,
    actions: (@Composable () -> Unit)? = null,
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(24.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            if (actions != null) {
                actions()
            }
        }
    }
}

@Composable
private fun SessionError(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddSetSheet(
    exercise: Exercise?,
    onAddSet: (Set) -> Unit,
    onDismiss: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val state = rememberModalBottomSheetState()
    ModalBottomSheet(sheetState = state, onDismissRequest = onDismiss) {
        AddSet(
            exercise = exercise,
            onDone = {
                onAddSet(it)
                scope.launch { state.hide() }.invokeOnCompletion {
                    if (!state.isVisible) onDismiss()
                }
            }
        )
    }
}

@PreviewLightDark
@Composable
private fun SessionDetailPreview() {
    KenkoTheme {
        val data = remember {
            val sets = listOf(
                Set(12, 55.0, Exercise("Bench", MuscleGroups.Chest)),
                Set(12, 55.0, Exercise("Bench", MuscleGroups.Chest)),
                Set(12, 55.0, Exercise("Bench", MuscleGroups.Chest)),
                Set(12, 55.0, Exercise("Incline Bench", MuscleGroups.Chest)),
                Set(12, 55.0, Exercise("Incline Bench", MuscleGroups.Chest)),
                Set(12, 55.0, Exercise("Incline Bench", MuscleGroups.Chest)),
                Set(12, 55.0, Exercise("Pec-Dec", MuscleGroups.Chest)),
                Set(12, 55.0, Exercise("Pec-Dec", MuscleGroups.Chest)),
                Set(12, 55.0, Exercise("Pec-Dec", MuscleGroups.Chest)),
            ).groupBy { it.exercise }
            SessionDetailState.Success(
                SessionUiData(
                    session = Session.SAMPLE,
                    sets = sets,
                    isToday = true
                )
            )
        }
        Surface(modifier = Modifier.fillMaxSize()) {
            SessionDetail(
                state = data,
                onBackPress = { /*TODO*/ },
                onSelectBottomSheet = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun SessionErrorPreview() {
    KenkoTheme {
        val data = remember {
            SessionDetailState.Error.InvalidSession
        }
        Surface(modifier = Modifier.fillMaxSize()) {
            SessionDetail(
                state = data,
                onBackPress = { /*TODO*/ },
                onSelectBottomSheet = {},
            )
        }
    }
}