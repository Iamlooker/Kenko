package com.looker.kenko.ui.sessionDetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.IconButtonDefaults
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
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Samples
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.Set
import com.looker.kenko.ui.addSet.AddSet
import com.looker.kenko.ui.components.BackButton
import com.looker.kenko.ui.components.SetItem
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.utils.DateTimeFormat
import com.looker.kenko.utils.formatDate
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
fun SessionDetails(onBackPress: () -> Unit) {
    val viewModel: SessionDetailViewModel = hiltViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()
    SessionDetail(
        state = state,
        onBackPress = onBackPress,
        onSelectBottomSheet = { viewModel.showBottomSheet(it) },
    )
    val exercise by viewModel.current.collectAsStateWithLifecycle()
    if (exercise != null) {
        AddSetSheet(
            exercise = exercise!!,
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
    when (state) {
        is SessionDetailState.Error -> {
            Column(Modifier.statusBarsPadding()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BackButton(onClick = onBackPress)
                }
                SessionError(
                    title = stringResource(state.title),
                    message = stringResource(state.errorMessage),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        SessionDetailState.Loading -> {
            Column(Modifier.statusBarsPadding()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BackButton(onClick = onBackPress)
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        is SessionDetailState.Success -> {
            val data = state.data
            SessionList(
                session = data.session,
                exerciseSets = data.sets,
                isEditable = data.isToday,
                onBackPress = onBackPress,
                onSelectBottomSheet = onSelectBottomSheet,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SessionList(
    session: Session,
    exerciseSets: Map<Exercise, List<Set>>?,
    isEditable: Boolean,
    onBackPress: () -> Unit,
    onSelectBottomSheet: (Exercise) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            bottom = WindowInsets.navigationBars.asPaddingValues(LocalDensity.current)
                .calculateBottomPadding() + 12.dp
        )
    ) {
        item {
            Header(
                performedOn = session.date,
                onBackPress = onBackPress,
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
    performedOn: LocalDate,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dayOfWeek = remember {
        formatDate(performedOn, DateTimeFormat.Day)
    }
    val date = remember {
        formatDate(performedOn, DateTimeFormat.Short)
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(MaterialTheme.colorScheme.inverseSurface)
            .statusBarsPadding()
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = -(16).dp),
            imageVector = KenkoIcons.ConcentricTriangles,
            tint = MaterialTheme.colorScheme.outline,
            contentDescription = null
        )
        Icon(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (16).dp),
            imageVector = KenkoIcons.Stack,
            tint = MaterialTheme.colorScheme.outline,
            contentDescription = null
        )
        Box(modifier = Modifier.matchParentSize()) {
            BackButton(
                modifier = Modifier.align(Alignment.TopStart),
                onClick = onBackPress,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface
                )
            )
            Column(
                modifier = Modifier.matchParentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(1F))
                Text(
                    text = dayOfWeek,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.inversePrimary,
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                )
                Spacer(modifier = Modifier.weight(1F))
            }
        }
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
    exercise: Exercise,
    onDismiss: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val state = rememberModalBottomSheetState()
    ModalBottomSheet(sheetState = state, onDismissRequest = onDismiss) {
        AddSet(
            exercise = exercise,
            onDone = {
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
            SessionDetailState.Success(
                SessionUiData(
                    session = Session.SAMPLE,
                    sets = Set.Samples.groupBy { it.exercise },
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