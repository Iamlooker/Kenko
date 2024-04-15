package com.looker.kenko.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun rememberDraggableTextFieldState(
    textFieldState: TextFieldState,
    onStopIncrement: () -> Unit,
    onHoldIncrement: (isRight: Boolean) -> Unit,
    supportingText: String? = null,
    swipeRange: DpRange = (-48).dp..96.dp,
    incrementRange: DpRange = (-42).dp..42.dp,
): DragState {
    return DragState(
        textFieldState,
        supportingText,
        swipeRange,
        incrementRange,
        onHoldIncrement,
        onStopIncrement
    )
}


@OptIn(ExperimentalFoundationApi::class)
data class DragState(
    val textFieldState: TextFieldState,
    val supportingText: String? = null,
    val swipeRange: DpRange,
    val incrementRange: DpRange,
    val onHoldIncrement: (isRight: Boolean) -> Unit,
    val onStopIncrement: () -> Unit,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableTextField(
    dragState: DragState,
    modifier: Modifier = Modifier,
) {
    val offset = remember {
        Animatable(0F)
    }

    val density = LocalDensity.current

    val bounds = remember(density, dragState.swipeRange) {
        with(density) {
            with(dragState.swipeRange) {
                start.toPx()..end.toPx()
            }
        }
    }

    val incrementZone = remember(density, bounds) {
        with(density) {
            with(dragState.incrementRange) {
                start.toPx()..end.toPx()
            }
        }
    }

    val isOutsideBound by remember {
        derivedStateOf {
            offset.value !in incrementZone
        }
    }
    LaunchedEffect(offset) {
        offset.updateBounds(bounds.start, bounds.endInclusive)
    }
    LaunchedEffect(isOutsideBound) {
        dragState.onStopIncrement()
        if (isOutsideBound) {
            dragState.onHoldIncrement(offset.value > 0)
        }
    }
    val scope = rememberCoroutineScope()
    val state = rememberDraggableState { delta ->
        val targetOffset = offset.value + delta
        val pullAmount = 1F
        val adjustedOffset = (1 - (pullAmount / 200F)) * targetOffset
        scope.launch {
            offset.animateTo(adjustedOffset, spring(dampingRatio = Spring.DampingRatioHighBouncy))
        }
    }
    Box(
        modifier = modifier
            .offset {
                IntOffset(offset.value.toInt(), 0)
            }
            .draggable(
                orientation = Orientation.Horizontal,
                state = state,
                onDragStopped = {
                    dragState.onStopIncrement()
                    offset.animateTo(0F, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                }
            )
            .requiredHeight(40.dp)
            .requiredWidthIn(40.dp)
            .wrapContentWidth()
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            state = dragState.textFieldState,
            lineLimits = TextFieldLineLimits.SingleLine,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSecondaryContainer),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorator = if (dragState.supportingText != null) kenkoTextDecorator(dragState.supportingText) else null,
            textStyle = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
        )
    }
}

private fun kenkoTextDecorator(supportingText: String) = TextFieldDecorator {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = supportingText,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )
        it()
    }
}

@Composable
fun kenkoTextFieldColor(): TextFieldColors = TextFieldDefaults.colors(
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    errorContainerColor = MaterialTheme.colorScheme.errorContainer
)