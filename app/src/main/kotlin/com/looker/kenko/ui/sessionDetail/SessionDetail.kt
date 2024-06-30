package com.looker.kenko.ui.sessionDetail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Samples
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.Set
import com.looker.kenko.ui.addSet.AddSet
import com.looker.kenko.ui.components.BackButton
import com.looker.kenko.ui.components.SwipeToDeleteBox
import com.looker.kenko.ui.extensions.normalizeInt
import com.looker.kenko.ui.extensions.plus
import com.looker.kenko.ui.planEdit.components.dayName
import com.looker.kenko.ui.sessionDetail.components.SetItem
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.utils.DateTimeFormat
import com.looker.kenko.utils.formatDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import java.text.BreakIterator
import java.text.StringCharacterIterator

@Composable
fun SessionDetails(
    viewModel: SessionDetailViewModel,
    onBackPress: () -> Unit,
    onHistoryClick: (LocalDate) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SessionDetail(
        state = state,
        onBackPress = onBackPress,
        onRemoveSet = viewModel::removeSet,
        onReferenceClick = viewModel::openReference,
        onSelectBottomSheet = viewModel::showBottomSheet,
        onHistoryClick = { onHistoryClick(viewModel.previousSessionDate) },
    )
    val exercise by viewModel.current.collectAsStateWithLifecycle()
    if (exercise != null) {
        AddSetSheet(
            exercise = exercise!!,
            onDismiss = viewModel::hideSheet
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionDetail(
    state: SessionDetailState,
    onBackPress: () -> Unit = {},
    onRemoveSet: (Set) -> Unit = {},
    onReferenceClick: (String) -> Unit = {},
    onSelectBottomSheet: (Exercise) -> Unit = {},
    onHistoryClick: () -> Unit = {},
) {
    when (state) {
        is SessionDetailState.Error -> {
            Column(Modifier.statusBarsPadding()) {
                TopAppBar(
                    navigationIcon = {
                        BackButton(onClick = onBackPress)
                    },
                    title = {},
                )
                SessionError(
                    title = stringResource(state.title),
                    message = stringResource(state.errorMessage),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        SessionDetailState.Loading -> {
            Column(Modifier.statusBarsPadding()) {
                TopAppBar(
                    navigationIcon = {
                        BackButton(onClick = onBackPress)
                    },
                    title = {},
                )
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
            SetsList(
                session = data.session,
                exerciseSets = data.sets,
                isEditable = data.isToday,
                hasPreviousSession = data.hasPreviousSession,
                onBackPress = onBackPress,
                onRemoveSet = onRemoveSet,
                onReferenceClick = onReferenceClick,
                onSelectBottomSheet = onSelectBottomSheet,
                onHistoryClick = onHistoryClick,
            )
        }
    }
}

@Composable
private fun SetsList(
    session: Session,
    exerciseSets: Map<Exercise, List<Set>>?,
    isEditable: Boolean,
    hasPreviousSession: Boolean,
    onBackPress: () -> Unit,
    onRemoveSet: (Set) -> Unit,
    onReferenceClick: (String) -> Unit,
    onSelectBottomSheet: (Exercise) -> Unit,
    onHistoryClick: () -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(360.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        contentPadding = WindowInsets.navigationBars.asPaddingValues(LocalDensity.current)
                + PaddingValues(bottom = 12.dp)
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Header(
                performedOn = session.date,
                onBackPress = onBackPress,
                actions = {
                    if (hasPreviousSession) {
                        IconButton(onClick = onHistoryClick) {
                            Icon(
                                imageVector = KenkoIcons.History,
                                contentDescription = null,
                            )
                        }
                    }
                }
            )
        }
        exerciseSets?.forEach { (exercise, sets) ->
            item(
                span = { GridItemSpan(maxLineSpan) },
            ) {
                StickyHeader(name = exercise.name) {
                    if (!exercise.reference.isNullOrBlank()) {
                        FilledTonalIconButton(onClick = { onReferenceClick(exercise.reference) }) {
                            Icon(imageVector = KenkoIcons.Lightbulb, contentDescription = null)
                        }
                    }
                    if (isEditable) {
                        FilledTonalIconButton(onClick = { onSelectBottomSheet(exercise) }) {
                            Icon(imageVector = KenkoIcons.Add, contentDescription = null)
                        }
                    }
                }
            }
            itemsIndexed(items = sets) { index, set ->
                SwipeToDeleteBox(
                    modifier = Modifier.animateItem(),
                    onDismiss = { onRemoveSet(set) },
                ) {
                    SetItem(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        set = set,
                        title = {
                            AnimatedSetIndex(index = index)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedSetIndex(
    index: Int,
) {
    AnimatedContent(
        targetState = index,
        transitionSpec = {
            if (targetState > initialState) {
                (slideInVertically { height -> height } + fadeIn()) togetherWith
                        (slideOutVertically { height -> -height } + fadeOut())
            } else {
                slideInVertically { height -> -height } + fadeIn() togetherWith
                        (slideOutVertically { height -> height } + fadeOut())
            } using SizeTransform(clip = false)
        }, label = ""
    ) { targetCount ->
        Text(text = normalizeInt(targetCount + 1))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Header(
    performedOn: LocalDate,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit,
) {
    val date = remember {
        formatDate(performedOn, DateTimeFormat.Short)
    }
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            BackButton(onClick = onBackPress)
        },
        title = {
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                SessionHeaderTitle(text = dayName(performedOn.dayOfWeek))
                SessionHeaderTitle(
                    text = date,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        },
        actions = actions,
    )
}

@Composable
private fun StickyHeader(
    name: String,
    actions: (@Composable RowScope.() -> Unit)? = null,
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
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.weight(1F))
            if (actions != null) {
                actions()
            }
        }
    }
}

@Composable
private fun SessionHeaderTitle(
    text: String,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalContentColor.current,
) {
    val breakIterator = remember(text) { BreakIterator.getCharacterInstance() }
    val typingDelayInMs = 50L

    var substringText by remember {
        mutableStateOf("")
    }
    LaunchedEffect(text) {
        delay(100)
        breakIterator.text = StringCharacterIterator(text)

        var nextIndex = breakIterator.next()
        while (nextIndex != BreakIterator.DONE) {
            substringText = text.subSequence(0, nextIndex).toString()
            nextIndex = breakIterator.next()
            delay(typingDelayInMs)
        }
    }
    Text(
        text = substringText,
        style = style,
        color = color,
    )
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
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
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
            SessionDetail(state = data)
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
            SessionDetail(state = data)
        }
    }
}
