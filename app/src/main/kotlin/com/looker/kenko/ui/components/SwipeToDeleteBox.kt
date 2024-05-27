package com.looker.kenko.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.theme.KenkoIcons
import kotlinx.coroutines.launch

@Composable
fun SwipeToDeleteBox(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var isDismissed by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }
    val state = rememberSwipeToDismissBoxState(
        initialValue = SwipeToDismissBoxValue.Settled,
        confirmValueChange = { newValue ->
            if (newValue != SwipeToDismissBoxValue.Settled) {
                showConfirmation = true
                false
            } else {
                isDismissed = newValue != SwipeToDismissBoxValue.Settled
                true
            }
        }
    )
    val isOutside by remember {
        derivedStateOf { state.targetValue != SwipeToDismissBoxValue.Settled }
    }
    val deleteBubbleSize = remember { Animatable(0F) }
    val coroutineScope = rememberCoroutineScope()

    if (showConfirmation) {
        ConfirmationDialog(
            onDismissRequest = { showConfirmation = false },
            onConfirmClick = {
                showConfirmation = false
                isDismissed = true
                onDismiss()
                coroutineScope.launch { state.snapTo(SwipeToDismissBoxValue.Settled) }
            },
            onDismiss = { showConfirmation = false }
        )
    }

    LaunchedEffect(isOutside, isDismissed) {
        if (isOutside) {
            deleteBubbleSize.animateTo(
                targetValue = 1080F,
                animationSpec = spring(
                    stiffness = StiffnessLow,
                )
            )
        } else {
            deleteBubbleSize.animateTo(
                targetValue = 0F,
                animationSpec = spring(
                    stiffness = StiffnessLow,
                )
            )
        }
        if (isDismissed) {
            onDismiss()
            state.snapTo(SwipeToDismissBoxValue.Settled)
        }
    }

    SwipeToDismissBox(
        modifier = modifier,
        state = state,
        backgroundContent = {
            val alignment by remember {
                derivedStateOf {
                    when (state.dismissDirection) {
                        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                        SwipeToDismissBoxValue.Settled -> Alignment.Center
                    }
                }
            }
            val backgroundColor = MaterialTheme.colorScheme.errorContainer
            Box(
                modifier = Modifier
                    .clipToBounds()
                    .fillMaxSize()
                    .drawWithCache {
                        val circleCenter = state.dismissDirection.toOffset()
                        onDrawBehind {
                            drawCircle(
                                color = backgroundColor,
                                center = circleCenter,
                                radius = deleteBubbleSize.value,
                            )
                        }
                    },
            ) {
                Icon(
                    modifier = Modifier
                        .align(alignment)
                        .padding(10.dp),
                    imageVector = KenkoIcons.Delete,
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    contentDescription = null
                )
            }
        },
    ) {
        content()
    }
}

@Composable
private fun ConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirmClick
            ) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(text = "Delete?")
        },
        text = {
            Text(text = "Are you sure?")
        },
        modifier = Modifier.padding(16.dp)
    )
}

private fun SwipeToDismissBoxValue.toOffset(): Offset = when (this) {
    SwipeToDismissBoxValue.StartToEnd -> Offset(0F, 0F)
    SwipeToDismissBoxValue.EndToStart -> Offset(1080F, 0F)
    SwipeToDismissBoxValue.Settled -> Offset(540F, 1776F)
}
