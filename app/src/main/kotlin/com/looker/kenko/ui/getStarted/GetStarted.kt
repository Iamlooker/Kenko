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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.ui.components.HealthQuotes
import com.looker.kenko.ui.components.TypingText
import com.looker.kenko.ui.extensions.PHI
import com.looker.kenko.ui.extensions.vertical
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GetStarted(onNext: () -> Unit) {
    val viewModel: GetStartedViewModel = hiltViewModel()

    val isOnboardingDone by viewModel.isOnboardingDone.collectAsStateWithLifecycle()
    val updatedOnNext by rememberUpdatedState(newValue = onNext)

    LaunchedEffect(isOnboardingDone) {
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
            modifier = Modifier.fillMaxSize()
        ) {
            val iconVisibility = remember { Animatable(-50F) }
            val buttonVisibility = remember { Animatable(0.75F) }
            LaunchedEffect(true) {
                buttonVisibility.animateTo(
                    targetValue = 0.85F,
                    animationSpec = spring()
                )
                launch {
                    iconVisibility.animateTo(
                        targetValue = 0F,
                        animationSpec = spring(
                            stiffness = Spring.StiffnessVeryLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    )
                }
                buttonVisibility.animateTo(
                    targetValue = 1F,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessVeryLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
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
                            buttonIcon = {
                                FilledTonalButton(
                                    modifier = Modifier
                                        .layoutId(ButtonID.Button)
                                        .graphicsLayer {
                                            scaleX = buttonVisibility.value
                                            scaleY = buttonVisibility.value
                                            translationY = (1F - buttonVisibility.value) * 15F
                                        },
                                    onClick = onNextClick,
                                    contentPadding = PaddingValues(
                                        vertical = 24.dp,
                                        horizontal = 40.dp
                                    )
                                ) {
                                    ButtonIcon(
                                        modifier = Modifier
                                            .graphicsLayer {
                                                translationX = iconVisibility.value
                                                rotationZ = iconVisibility.value
                                            }
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
private fun ButtonGroup(
    buttonIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Layout(
        modifier = modifier,
        content = {
            Icon(
                modifier = Modifier.layoutId(ButtonID.Cloud),
                imageVector = KenkoIcons.Cloud,
                contentDescription = null
            )
            Icon(
                modifier = Modifier.layoutId(ButtonID.Arrow1),
                imageVector = KenkoIcons.Arrow1,
                contentDescription = null
            )
            Icon(
                modifier = Modifier.layoutId(ButtonID.Arrow2),
                imageVector = KenkoIcons.Arrow2,
                contentDescription = null
            )
            Icon(
                modifier = Modifier.layoutId(ButtonID.Arrow3),
                imageVector = KenkoIcons.Arrow3,
                contentDescription = null
            )
            Icon(
                modifier = Modifier.layoutId(ButtonID.Arrow4),
                imageVector = KenkoIcons.Arrow4,
                contentDescription = null
            )
            buttonIcon()
        }
    ) { measurables, constraints ->
        lateinit var cloud: Measurable
        lateinit var arrow1: Measurable
        lateinit var arrow2: Measurable
        lateinit var arrow3: Measurable
        lateinit var arrow4: Measurable
        lateinit var button: Measurable
        measurables.forEach { measurable ->
            when (measurable.layoutId) {
                ButtonID.Button -> button = measurable
                ButtonID.Cloud -> cloud = measurable
                ButtonID.Arrow1 -> arrow1 = measurable
                ButtonID.Arrow2 -> arrow2 = measurable
                ButtonID.Arrow3 -> arrow3 = measurable
                ButtonID.Arrow4 -> arrow4 = measurable
                else -> error("Unknown Element")
            }
        }
        val cloudPlaceable = cloud.measure(constraints)
        val arrow1Placeable = arrow1.measure(constraints)
        val arrow2Placeable = arrow2.measure(constraints)
        val arrow3Placeable = arrow3.measure(constraints)
        val arrow4Placeable = arrow4.measure(constraints)
        val buttonPlaceable = button.measure(constraints)
        val width = 360.dp.toPx().toInt()
        layout(width, 162.dp.toPx().toInt()) {
            val x = (width / 2) - (buttonPlaceable.width / 2)
            cloudPlaceable.placeRelative(16.dp.toPx().toInt(), 31.dp.toPx().toInt())
            arrow1Placeable.placeRelative(148.dp.toPx().toInt(), 2.dp.toPx().toInt())
            arrow2Placeable.placeRelative(200.dp.toPx().toInt(), 0.dp.toPx().toInt())
            arrow3Placeable.placeRelative(270.dp.toPx().toInt(), 26.dp.toPx().toInt())
            arrow4Placeable.placeRelative(290.dp.toPx().toInt(), 100.dp.toPx().toInt())
            buttonPlaceable.placeRelative(x, 68.dp.toPx().toInt())
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

enum class ButtonID {
    Cloud, Arrow1, Arrow2, Arrow3, Arrow4, Button
}

@Composable
private fun ButtonIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector = KenkoIcons.ArrowForward,
) {
    Icon(
        modifier = modifier
            .background(MaterialTheme.colorScheme.onSecondaryContainer, CircleShape)
            .padding(8.dp),
        imageVector = icon,
        tint = MaterialTheme.colorScheme.secondaryContainer,
        contentDescription = ""
    )
}

@Preview
@Composable
private fun HeroPreview() {
    KenkoTheme {
        ButtonGroup(buttonIcon = { ButtonIcon() })
    }
}

@Preview
@PreviewScreenSizes
@Composable
private fun GetStartedPreview() {
    KenkoTheme {
        GetStarted(isOnboardingDone = false, onNextClick = {})
    }
}
