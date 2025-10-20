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

package com.looker.kenko.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.theme.KenkoIcons
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeToDeleteBox(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val actionWidth = 96.dp
    val actionWidthPx = with(density) { actionWidth.toPx() }
    val anchors = remember {
        DraggableAnchors {
            DragPositions.Settle at 0F
            DragPositions.End at actionWidthPx
        }
    }
    val state = remember {
        AnchoredDraggableState(
            initialValue = DragPositions.Settle,
            anchors = anchors,
        )
    }
    val isOutsideBound by remember {
        derivedStateOf {
            state.currentValue == DragPositions.End
        }
    }
    val background by animateColorAsState(
        targetValue = if (isOutsideBound) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainer
        },
        label = "",
    )
    Box(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .drawBehind { drawRect(background) },
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .align(Alignment.CenterEnd)
                .clickable {
                    scope.launch {
                        state.snapTo(DragPositions.Settle)
                        onDismiss()
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier
                    .requiredWidth(actionWidth)
                    .align(Alignment.CenterEnd),
                painter = KenkoIcons.Delete,
                tint = MaterialTheme.colorScheme.onErrorContainer,
                contentDescription = null,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset {
                    IntOffset(
                        x = -state
                            .requireOffset()
                            .roundToInt(),
                        y = 0,
                    )
                }
                .graphicsLayer {
                    val offset = state.requireOffset()
                    val scale = 1 - (offset / actionWidthPx)
                    val cornerRadius = actionWidthPx / 4F
                    clip = true
                    scaleX = scale.coerceAtLeast(0.9F)
                    scaleY = scale.coerceAtLeast(0.9F)
                    shape = RoundedCornerShape(cornerRadius * ((offset / actionWidthPx)))
                }
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Horizontal,
                    reverseDirection = true,
                    flingBehavior = AnchoredDraggableDefaults.flingBehavior(
                        state = state,
                        positionalThreshold = { distance: Float -> distance * 0.5F },
                        animationSpec = tween(),
                    ),
                ),
        ) {
            content()
        }
    }
}

@Composable
fun disableScrollConnection() = remember {
    object : NestedScrollConnection {
        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            return available
        }
    }
}

private enum class DragPositions {
    Settle,
    End,
}
