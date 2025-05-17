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

package com.looker.kenko.ui.addSet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
    options: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Next,
    ),
) {
    BasicTextField(
        modifier = modifier
            .requiredHeight(40.dp)
            .requiredWidthIn(40.dp)
            .wrapContentWidth()
            .graphicsLayer { translationX = dragState.offset.value }
            .draggable(
                orientation = Orientation.Horizontal,
                state = dragState.state,
                startDragImmediately = true,
                onDragStopped = { dragState.onDragStop(it) },
            )
            .clip(CircleShape)
            .background(containerColor),
        state = textFieldState,
        lineLimits = TextFieldLineLimits.SingleLine,
        inputTransformation = inputTransformation,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSecondaryContainer),
        keyboardOptions = options,
        decorator = kenkoTextDecorator(supportingText),
        textStyle = style.copy(textAlign = TextAlign.Center),
    )
}
