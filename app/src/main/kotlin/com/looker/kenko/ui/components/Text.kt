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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay
import java.text.BreakIterator
import java.text.StringCharacterIterator
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TypingText(
    text: String,
    startTyping: Boolean = true,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalContentColor.current,
    initialDelay: Duration = 100.milliseconds,
    typingDelay: Duration = 50.milliseconds,
    onCompleteListener: (() -> Unit)? = null,
) {
    val breakIterator = remember(text) { BreakIterator.getCharacterInstance() }

    var substringText by remember {
        mutableStateOf("")
    }
    LaunchedEffect(text, startTyping) {
        if (startTyping) {
            delay(initialDelay)
            breakIterator.text = StringCharacterIterator(text)

            var nextIndex = breakIterator.next()
            while (nextIndex != BreakIterator.DONE) {
                substringText = text.subSequence(0, nextIndex).toString()
                nextIndex = breakIterator.next()
                delay(typingDelay)
            }
            onCompleteListener?.invoke()
        }
    }
    Text(
        text = substringText,
        style = style,
        color = color,
    )
}
