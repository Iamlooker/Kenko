package com.looker.kenko.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.theme.KenkoIcons

@Composable
fun SwipeToDeleteBox(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var isDismissed by remember { mutableStateOf(false) }
    val state = rememberSwipeToDismissBoxState(
        initialValue = SwipeToDismissBoxValue.Settled,
        confirmValueChange = {
            isDismissed = it != SwipeToDismissBoxValue.Settled
            true
        }
    )
    val isOutside by remember {
        derivedStateOf { state.targetValue != SwipeToDismissBoxValue.Settled }
    }
    val deleteBubbleSize = remember { Animatable(0F) }
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

context(CacheDrawScope)
fun SwipeToDismissBoxValue.toOffset(): Offset = when (this) {
    SwipeToDismissBoxValue.StartToEnd -> center.copy(x = 0F)
    SwipeToDismissBoxValue.EndToStart -> center.copy(x = size.width)
    SwipeToDismissBoxValue.Settled -> center
}

val CacheDrawScope.center: Offset
    get() = Offset(size.width / 2, size.height / 2)
