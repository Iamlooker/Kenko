package com.looker.kenko.ui.components.texture

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color

val defaultTextureColor: Color
    @Composable
    get() = MaterialTheme.colorScheme.surfaceContainer

fun Modifier.dottedGradient(
    color: Color,
    drawRatio: Float = 0.3F,
    start: GradientStart = GradientStart.TopLeft,
): Modifier = drawWithCache {
        dottedTexture(color, drawDistanceRatio = drawRatio, start = start)
}
