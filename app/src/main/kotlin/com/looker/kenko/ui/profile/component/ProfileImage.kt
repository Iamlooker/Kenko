package com.looker.kenko.ui.profile.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ProfileImage(
    imagePath: String,
    modifier: Modifier = Modifier,
    animationDuration: Int = 8_000,
) {
    val infiniteTransition = rememberInfiniteTransition("infinite outline movement")
    val morphProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(animationDuration),
            repeatMode = RepeatMode.Reverse
        ),
        label = "morphProgress"
    )
    val imageRotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(animationDuration, easing = LinearEasing),
        ),
        label = "imageRotation"
    )

    AsyncImage(
        model = imagePath,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .graphicsLayer {
                clip = true
                shape = ProfileImageMorphing(
                    morphProgress.value,
                    imageRotation.value
                )
            }
    )
}
