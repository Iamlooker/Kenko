package com.looker.kenko.ui.getStarted

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.looker.kenko.R
import com.looker.kenko.ui.components.HealthQuotes
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
            launch {
                buttonVisibility.animateTo(
                    targetValue = 1F,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessVeryLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
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
            ButtonGroup(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                content = {
                    Button(
                        modifier = Modifier
                            .layoutId(ButtonID.Button)
                            .graphicsLayer {
                                scaleX = buttonVisibility.value
                                scaleY = buttonVisibility.value
                                translationY = (1F - buttonVisibility.value) * 15F
                            },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary,
                        ),
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
                        Text(text = stringResource(R.string.label_lets_go))
                    }
                },
            )
            HealthQuotes(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
private fun ButtonGroup(
    content: @Composable () -> Unit,
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
            content()
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

private enum class ButtonID {
    Cloud, Arrow1, Arrow2, Arrow3, Arrow4, Button
}

@Composable
private fun ButtonIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector = KenkoIcons.ArrowForward,
) {
    Icon(
        modifier = modifier
            .background(MaterialTheme.colorScheme.onTertiary, CircleShape)
            .padding(8.dp),
        imageVector = icon,
        tint = MaterialTheme.colorScheme.tertiary,
        contentDescription = ""
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
