package com.looker.kenko.ui.components.texture

import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

private const val DOT_SIZE = 3F
private const val DOT_GAP = 16F

fun CacheDrawScope.dottedTexture(
    color: Color,
    drawDistanceRatio: Float,
    start: GradientStart = GradientStart.TopLeft,
): DrawResult {
    val (horizontalDots, verticalDots) = getDotsPerSize(size)
    val drawDistance = drawDistanceRatio * (size.center * 2F).getDistance()
    val startPosition = when (start) {
        GradientStart.BottomLeft -> Offset.Zero.copy(y = size.height)
        GradientStart.BottomRight -> Offset(size.width, size.height)
        is GradientStart.Custom -> start.offset(this)
        GradientStart.TopLeft -> Offset.Zero
        GradientStart.TopRight -> Offset.Zero.copy(x = size.width)
    }
    val centerAndAlpha = mutableMapOf<Offset, Float>()
    val random = Random.Default
    repeat(verticalDots) { y ->
        val isStaggered = y % 2 == 0
        val verticalPosition = (y + 1) * (DOT_SIZE + DOT_GAP)
        val staggeredDistance = if (isStaggered) (DOT_GAP / 2) + DOT_SIZE else 0F
        repeat(horizontalDots) xRepeat@{ x ->
            if (random.nextBoolean()) {
                val center = Offset(
                    x = (x * (DOT_SIZE + DOT_GAP)) + staggeredDistance,
                    y = verticalPosition
                )
                val alpha =
                    (1F - (center - startPosition).getDistance() / drawDistance).coerceAtLeast(0F)
                if (alpha > 0F) {
                    centerAndAlpha[center] = alpha

                }
            }
        }
    }
    return onDrawBehind {
        centerAndAlpha.forEach { (center, alpha) ->
            drawCircle(
                color = color.copy(alpha = alpha),
                radius = DOT_SIZE,
                center = center,
            )
        }
    }
}

private fun getDotsPerSize(size: Size): Pair<Int, Int> {
    val horizontalDots = (size.width / (DOT_SIZE + DOT_GAP)).toInt()
    val verticalDots = (size.height / (DOT_SIZE + DOT_GAP)).toInt()
    return horizontalDots to verticalDots
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
