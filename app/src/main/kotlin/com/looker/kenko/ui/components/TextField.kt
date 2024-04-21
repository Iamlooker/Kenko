package com.looker.kenko.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

fun kenkoTextDecorator(supportingText: String) = TextFieldDecorator {
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