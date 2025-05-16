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

package com.looker.kenko.ui.getStarted

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.looker.kenko.R
import com.looker.kenko.ui.components.HealthQuotes
import com.looker.kenko.ui.components.TickerText
import com.looker.kenko.ui.components.TypingText
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.header
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GetStarted(onNext: () -> Unit) {
    GetStarted(onNextClick = onNext)
}

@Composable
private fun GetStarted(
    modifier: Modifier = Modifier,
    onNextClick: () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        val iconVisibility = remember { Animatable(-50F) }
        val buttonVisibility = remember { Animatable(0.75F) }
        LaunchedEffect(true) {
            buttonVisibility.animateTo(
                targetValue = 0.85F,
                animationSpec = spring(),
            )
            launch {
                iconVisibility.animateTo(
                    targetValue = 0F,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessVeryLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                    ),
                )
            }
            launch {
                buttonVisibility.animateTo(
                    targetValue = 1F,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessVeryLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                    ),
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            var startShowingFirstMeaning by remember { mutableStateOf(false) }
            var startShowingSecondMeaning by remember { mutableStateOf(false) }
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                text = stringResource(R.string.label_kenko),
                style = MaterialTheme.typography.header(),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 18.dp),
            )
            TypingText(
                text = stringResource(R.string.label_kenko_jp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                typingDelay = 10.milliseconds,
                onCompleteListener = { startShowingFirstMeaning = true },
                modifier = Modifier.padding(horizontal = 18.dp),
            )
            TypingText(
                text = stringResource(R.string.label_kenko_meaning),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.outline,
                startTyping = startShowingFirstMeaning,
                typingDelay = 10.milliseconds,
                initialDelay = 0.milliseconds,
                onCompleteListener = { startShowingSecondMeaning = true },
                modifier = Modifier.padding(horizontal = 18.dp),
            )
            TypingText(
                text = stringResource(R.string.label_kenko_meaning_ALT),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.outline,
                startTyping = startShowingSecondMeaning,
                typingDelay = 10.milliseconds,
                initialDelay = 0.milliseconds,
                modifier = Modifier.padding(horizontal = 18.dp),
            )
            Spacer(modifier = Modifier.weight(1F))
            TickerText(
                texts = stringArrayResource(R.array.label_features),
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6F)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(1F))
                Text(
                    text = stringResource(R.string.label_boarding_quote),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.weight(1F))
                Button(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = buttonVisibility.value
                            scaleY = buttonVisibility.value
                            translationY = (1F - buttonVisibility.value) * 15F
                        },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.inverseSurface,
                        contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    ),
                    onClick = onNextClick,
                    contentPadding = PaddingValues(
                        vertical = 24.dp,
                        horizontal = 40.dp,
                    ),
                ) {
                    ButtonIcon(
                        modifier = Modifier
                            .graphicsLayer {
                                translationX = iconVisibility.value
                                rotationZ = iconVisibility.value
                            },
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                        iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.label_lets_go))
                }
                HealthQuotes(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}

@Composable
private fun ButtonIcon(
    iconColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    Icon(
        modifier = modifier
            .background(backgroundColor, CircleShape)
            .padding(8.dp),
        painter = KenkoIcons.ArrowForward,
        tint = iconColor,
        contentDescription = "",
    )
}

@Preview
@PreviewScreenSizes
@Composable
private fun GetStartedPreview() {
    KenkoTheme {
        GetStarted(onNextClick = {})
    }
}
