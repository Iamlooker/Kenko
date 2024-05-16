package com.looker.kenko.ui.components.texture

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.gradient(
    vararg stops: Pair<Float, Color>,
    start: GradientStart = GradientStart.TopLeft,
    type: GradientType = GradientType.Radial,
): Modifier = drawWithCache {
    val startPosition = when (start) {
        GradientStart.BottomLeft -> Offset.Zero.copy(y = size.height)
        GradientStart.BottomRight -> Offset(size.width, size.height)
        is GradientStart.Custom -> start.offset(this@drawWithCache)
        GradientStart.TopLeft -> Offset.Zero
        GradientStart.TopRight -> Offset.Zero.copy(x = size.width)
    }
    val brush = Brush
    val brushType = when (type) {
        GradientType.Horizontal -> brush.horizontalGradient(
            *stops,
            startX = startPosition.x,
            endX = size.width - startPosition.x,
        )

        GradientType.Vertical -> brush.verticalGradient(
            *stops,
            startY = startPosition.y,
            endY = size.height - startPosition.y,
        )
        GradientType.Radial -> brush.radialGradient(
            *stops,
            center = startPosition,
        )
    }
    onDrawBehind {
        drawRect(brush = brushType)
    }
}