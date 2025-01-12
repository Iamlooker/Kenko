package com.looker.kenko.ui.addSet.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.components.kenkoTextDecorator

@Composable
fun DraggableTextField(
    dragState: DragState,
    textFieldState: TextFieldState,
    supportingText: String,
    inputTransformation: InputTransformation,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    textColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    style: TextStyle = MaterialTheme.typography.titleMedium.copy(color = textColor),
) {
    val offset = remember { dragState.offset }
    val state: DraggableState = remember { dragState.state }

    BasicTextField(
        modifier = modifier
            .requiredHeight(40.dp)
            .requiredWidthIn(40.dp)
            .wrapContentWidth()
            .offset { IntOffset(offset.value.toInt(), 0) }
            .draggable(
                orientation = Orientation.Horizontal,
                state = state,
                startDragImmediately = true,
                onDragStopped = {
                    dragState.events.onStop()
                    offset.animateTo(
                        targetValue = 0F,
                        animationSpec = dragState.mediumBouncySpring,
                        initialVelocity = it,
                    )
                },
            )
            .clip(CircleShape)
            .background(containerColor),
        state = textFieldState,
        lineLimits = TextFieldLineLimits.SingleLine,
        inputTransformation = inputTransformation,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSecondaryContainer),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorator = kenkoTextDecorator(supportingText),
        textStyle = style.copy(textAlign = TextAlign.Center),
    )
}
