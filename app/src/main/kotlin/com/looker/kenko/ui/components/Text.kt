package com.looker.kenko.ui.components

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TypingText(
    text: String,
    modifier: Modifier = Modifier,
    startTyping: Boolean = true,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalContentColor.current,
    initialDelay: Duration = 100.milliseconds,
    typingDelay: Duration = 50.milliseconds,
    onCompleteListener: (() -> Unit)? = null,
) {
    var substringText by remember { mutableStateOf("") }
    val textArray = remember(text) { CharArray(text.length) }
    LaunchedEffect(text, startTyping) {
        if (startTyping) {
            delay(initialDelay)
            for (i in textArray.indices) {
                textArray[i] = text[i]
                substringText = textArray.concatToString()
                delay(typingDelay)
            }
            onCompleteListener?.invoke()
        }
    }
    Text(
        modifier = modifier,
        text = substringText,
        style = style,
        color = color,
    )
}
