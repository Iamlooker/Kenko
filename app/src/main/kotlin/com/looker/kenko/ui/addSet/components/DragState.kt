/*
 * Copyright (C) 2025 LooKeR & Contributors
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

package com.looker.kenko.ui.addSet.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.components.DpRange
import com.looker.kenko.ui.components.contains
import com.looker.kenko.ui.components.rangeTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val OFFSET_DAMPING = 0.998F

@Stable
class DragState(
    density: Density,
    swipeRange: DpRange,
    noIncrementRange: DpRange,
    incrementDelay: Long,
    private val events: BoundReached,
    private val scope: CoroutineScope,
) {

    val offset: Animatable<Float, AnimationVector1D> = Animatable(0F)

    private val highBouncySpring = spring<Float>(Spring.DampingRatioHighBouncy)
    private val mediumBouncySpring = spring<Float>(Spring.DampingRatioMediumBouncy)

    val state: DraggableState = DraggableState { delta ->
        val targetOffset = offset.value + delta
        val adjustedOffset = OFFSET_DAMPING * targetOffset
        scope.launch { offset.animateTo(adjustedOffset, highBouncySpring) }
    }

    @Stable
    fun onDragStop(velocity: Float) {
        scope.launch {
            offset.animateTo(
                targetValue = 0F,
                animationSpec = mediumBouncySpring,
                initialVelocity = velocity,
            )
        }
    }

    init {
        with(density) {
            offset.updateBounds(
                swipeRange.start.toPx(),
                swipeRange.end.toPx(),
            )
            scope.launch {
                while (true) {
                    val isOutsideBounds = offset.value !in noIncrementRange
                    if (isOutsideBounds) {
                        val direction = if (offset.value > 0) Direction.Right else Direction.Left
                        events.onReached(direction)
                    }
                    delay(incrementDelay)
                }
            }
        }
    }
}

@Composable
fun rememberDraggableTextFieldState(
    onBoundReached: BoundReached,
    incrementDelay: Long = 200,
    swipeRange: DpRange = (-48).dp..96.dp,
    noIncrementRange: DpRange = (-24).dp..24.dp,
): DragState {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    return remember {
        DragState(
            density = density,
            swipeRange = swipeRange,
            noIncrementRange = noIncrementRange,
            events = onBoundReached,
            scope = scope,
            incrementDelay = incrementDelay,
        )
    }
}

@Stable
fun interface BoundReached {
    fun onReached(direction: Direction)
}

@JvmInline
@Immutable
value class Direction private constructor(val value: Int) {
    companion object {
        val Left = Direction(1)
        val Right = Direction(2)
    }

    override fun toString(): String {
        return when (this) {
            Left -> "Left"
            Right -> "Right"
            else -> "Invalid"
        }
    }
}
