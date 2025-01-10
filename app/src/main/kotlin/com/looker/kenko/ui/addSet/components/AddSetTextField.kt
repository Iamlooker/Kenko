package com.looker.kenko.ui.addSet.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.components.kenkoTextDecorator

@Composable
fun AddSetTextField(
    textFieldState: TextFieldState,
    supportingText: String,
    inputTransformation: InputTransformation,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
) {
    BasicTextField(
        modifier = modifier
            .requiredHeight(48.dp)
            .requiredWidthIn(48.dp)
            .wrapContentWidth()
            .clip(CircleShape)
            .background(containerColor),
        state = textFieldState,
        lineLimits = TextFieldLineLimits.SingleLine,
        inputTransformation = inputTransformation,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSecondaryContainer),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorator = kenkoTextDecorator(supportingText),
        textStyle = MaterialTheme.typography.titleMedium.copy(
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
    )
}
