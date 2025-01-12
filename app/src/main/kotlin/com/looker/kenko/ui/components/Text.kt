package com.looker.kenko.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
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

@Composable
fun TickerText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelLarge,
    color: Color = LocalContentColor.current,
) {
    val tickerMarquee = remember {
        List(10) {
            text
        }.joinToString(
            separator = " ${Typography.bullet} ",
            prefix = " ${Typography.bullet} ",
        )
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .basicMarquee(
                    initialDelayMillis = 0,
                    iterations = Int.MAX_VALUE,
                ),
            text = tickerMarquee,
            style = style,
            color = color,
        )
    }
}

@Composable
fun TickerText(
    texts: Array<String>,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelLarge,
    color: Color = LocalContentColor.current,
) {
    val tickerMarquee = remember {
        texts.joinToString(
            separator = " ${Typography.bullet} ",
            prefix = " ${Typography.bullet} ",
        )
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .basicMarquee(
                    initialDelayMillis = 0,
                    iterations = Int.MAX_VALUE,
                ),
            text = tickerMarquee,
            style = style,
            color = color,
        )
    }
}

