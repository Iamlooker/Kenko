package com.looker.kenko.ui.components.texture

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color

private const val NOISE_SCALE = 0.004

fun Modifier.dottedGradient(
    color: Color,
    drawRatio: Float = 0.5F,
    noiseScale: Double = NOISE_SCALE,
    start: GradientStart = GradientStart.TopLeft,
): Modifier = drawWithCache {
    dottedTexture(
        color = color,
        drawDistanceRatio = drawRatio,
        start = start,
        noiseScale = noiseScale,
    )
}
