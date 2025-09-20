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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.looker.kenko.R
import com.looker.kenko.ui.components.HealthQuotes
import com.looker.kenko.ui.components.TypingText
import com.looker.kenko.ui.extensions.PHI
import com.looker.kenko.ui.extensions.vertical
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GetStartedOld(onNext: () -> Unit) {
    val viewModel: GetStartedOldViewModel = hiltViewModel()

    val isOnboardingDone = viewModel.isOnboardingDone
    val updatedOnNext by rememberUpdatedState(newValue = onNext)

    LaunchedEffect(Unit) {
        if (isOnboardingDone) {
            delay(300)
            updatedOnNext()
        }
    }

    GetStarted(
        isOnboardingDone = isOnboardingDone,
        onNextClick = updatedOnNext,
    )
}

@Composable
private fun GetStarted(
    isOnboardingDone: Boolean,
    onNextClick: () -> Unit,
) {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize(),
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
                buttonVisibility.animateTo(
                    targetValue = 1F,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessVeryLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                    ),
                )
            }
            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 50.dp, y = 56.dp)
                    .graphicsLayer {
                        translationX = iconVisibility.value * 2
                        rotationZ = iconVisibility.value
                    },
                imageVector = KenkoIcons.Dawn,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null,
            )
            HeroTitle(modifier = Modifier.align(Alignment.CenterStart))
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (!isOnboardingDone) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.secondary) {
                        ButtonGroup(
                            content = {
                                FilledTonalButton(
                                    modifier = Modifier
                                        .graphicsLayer {
                                            scaleX = buttonVisibility.value
                                            scaleY = buttonVisibility.value
                                            translationY = (1F - buttonVisibility.value) * 15F
                                        },
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
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    TypingText(text = stringResource(R.string.label_lets_go))
                                }
                            },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                HealthQuotes()
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun HeroTitle(modifier: Modifier = Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy((-12).dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                modifier = Modifier.vertical(),
                text = stringResource(R.string.label_kenko).uppercase(),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.tertiary,
            )
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .aspectRatio(1 / PHI)
                    .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape),
            )
        }
        Column {
            var startShowingFirstMeaning by remember { mutableStateOf(false) }
            var startShowingSecondMeaning by remember { mutableStateOf(false) }
            TypingText(
                text = stringResource(R.string.label_kenko_jp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                typingDelay = 10.milliseconds,
                onCompleteListener = { startShowingFirstMeaning = true },
            )
            TypingText(
                text = stringResource(R.string.label_kenko_meaning),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                startTyping = startShowingFirstMeaning,
                typingDelay = 10.milliseconds,
                initialDelay = 0.milliseconds,
                onCompleteListener = { startShowingSecondMeaning = true },
            )
            TypingText(
                text = stringResource(R.string.label_kenko_meaning_ALT),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                startTyping = startShowingSecondMeaning,
                typingDelay = 10.milliseconds,
                initialDelay = 0.milliseconds,
            )
        }
    }
}

@Composable
private fun ButtonIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        modifier = modifier
            .background(MaterialTheme.colorScheme.onSecondaryContainer, CircleShape)
            .padding(8.dp),
        painter = KenkoIcons.ArrowForward,
        tint = MaterialTheme.colorScheme.secondaryContainer,
        contentDescription = "",
    )
}

@Preview
@PreviewScreenSizes
@Composable
private fun GetStartedPreview() {
    KenkoTheme {
        GetStarted(isOnboardingDone = false, onNextClick = {})
    }
}
