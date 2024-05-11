package com.looker.kenko.ui.components.texture

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.gradient(
    vararg stops: Pair<Float, Color>,
    start: GradientStart = GradientStart.TopLeft,
): Modifier = drawWithCache {
    onDrawBehind {
        val startPosition = when (start) {
            GradientStart.BottomLeft -> Offset.Zero.copy(y = size.height)
            GradientStart.BottomRight -> Offset(size.width, size.height)
            is GradientStart.Custom -> start.offset(this@drawWithCache)
            GradientStart.TopLeft -> Offset.Zero
            GradientStart.TopRight -> Offset.Zero.copy(x = size.width)
        }
        drawRect(
            brush = Brush.radialGradient(
                *stops,
                center = startPosition,
            ),
        )
    }
}