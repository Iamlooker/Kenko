package com.looker.kenko.ui.components.texture

import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.sqrt

private const val DOT_SIZE = 1F
private const val DOT_GAP = 3F

fun CacheDrawScope.dottedTexture(
    color: Color,
    drawDistanceRatio: Float,
    noiseScale: Double,
    start: GradientStart = GradientStart.TopLeft,
): DrawResult {
    val dots = getDotsPerSize(size)
    val drawDistance = (size.center * 2F * drawDistanceRatio).getDistanceSquared()
    val startPosition = when (start) {
        GradientStart.BottomLeft -> Offset.Zero.copy(y = size.height)
        GradientStart.BottomRight -> Offset(size.width, size.height)
        is GradientStart.Custom -> start.offset(this)
        GradientStart.TopLeft -> Offset.Zero
        GradientStart.TopRight -> Offset.Zero.copy(x = size.width)
    }
    val centerAndAlpha = dots.associateWith { center ->
        val distance = (center - startPosition).getDistanceSquared() / drawDistance
        val fadeFactor = max(0F, 0.7F - sqrt(distance))
        val noise = noise2D(
            center.x * noiseScale,
            center.y * noiseScale,
        )
        noise.toFloat() * fadeFactor
    }
    return onDrawBehind {
        centerAndAlpha.forEach { (center, alpha) ->
            drawCircle(
                color = color.copy(alpha = alpha),
                radius = DOT_SIZE,
                center = center,
                blendMode = BlendMode.Difference,
            )
        }
    }
}

private fun getDotsPerSize(size: Size): List<Offset> {
    val horizontalDots = (size.width / (DOT_SIZE + DOT_GAP)).toInt()
    val verticalDots = (size.height / (DOT_SIZE + DOT_GAP)).toInt()
    return List(horizontalDots * verticalDots) { index ->
        Offset(
            x = (index % horizontalDots) * (DOT_SIZE + DOT_GAP),
            y = (index / horizontalDots) * (DOT_SIZE + DOT_GAP)
        )
    }
}

sealed interface GradientStart {

    data object TopLeft : GradientStart

    data object TopRight : GradientStart

    data object BottomLeft : GradientStart

    data object BottomRight : GradientStart

    data class Custom(val offset: CacheDrawScope.() -> Offset) : GradientStart
}

sealed interface GradientType {

    data object Vertical : GradientType

    data object Horizontal : GradientType

    data object Radial : GradientType

}
