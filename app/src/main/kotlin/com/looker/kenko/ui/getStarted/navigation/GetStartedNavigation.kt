package com.looker.kenko.ui.getStarted.navigation

import android.graphics.Rect
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.animateZoomBy
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.looker.kenko.ui.getStarted.GetStarted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sign
import kotlin.math.sin

const val GET_STARTED_ROUTE = "get_started"

// We never navigate "TO" Get Started Screen

fun NavGraphBuilder.getStarted(onNext: () -> Unit) {
    composable(
        route = GET_STARTED_ROUTE,
    ) {
//        SnapFlingBehaviorSnapPosition()
//        OverscrollSample()
//        AndroidExternalSurfaceColors()
//        TransformableSample()
//        TransformableSampleInsideScroll()
        GetStarted(onNext)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransformableSampleInsideScroll() {
    Row(
        Modifier
            .size(width = 120.dp, height = 100.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        // first child of the scrollable row is a transformable
        Box(
            Modifier
                .size(100.dp)
                .clipToBounds()
                .background(Color.LightGray)
        ) {
            // set up all transformation states
            var scale by remember { mutableStateOf(1f) }
            var rotation by remember { mutableStateOf(0f) }
            var offset by remember { mutableStateOf(Offset.Zero) }
            val coroutineScope = rememberCoroutineScope()
            // let's create a modifier state to specify how to update our UI state defined above
            val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
                // note: scale goes by factor, not an absolute difference, so we need to multiply it
                // for this example, we don't allow downscaling, so cap it to 1f
                scale = max(scale * zoomChange, 1f)
                rotation += rotationChange
                offset += offsetChange
            }
            Box(
                Modifier
                    // apply pan offset state as a layout transformation before other modifiers
                    .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                    // add transformable to listen to multitouch transformation events after offset
                    // To make sure our transformable work well within pager or scrolling lists,
                    // disallow panning if we are not zoomed in.
                    .transformable(state = state, canPan = { scale != 1f })
                    // optional for example: add double click to zoom
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                coroutineScope.launch { state.animateZoomBy(4f) }
                            }
                        )
                    }
                    .fillMaxSize()
                    .border(1.dp, Color.Green),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "\uD83C\uDF55",
                    fontSize = 32.sp,
                    // apply other transformations like rotation and zoom on the pizza slice emoji
                    modifier = Modifier.graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        rotationZ = rotation
                    }
                )
            }
        }
        // other children are just colored boxes
        Box(
            Modifier
                .size(100.dp)
                .background(Color.Red)
                .border(2.dp, Color.Black)
        )
    }
}

@Composable
fun TransformableSample() {
    Box(
        Modifier
            .size(200.dp)
            .clipToBounds()
            .background(Color.LightGray)
    ) {
        // set up all transformation states
        var scale by remember { mutableStateOf(1f) }
        var rotation by remember { mutableStateOf(0f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        val coroutineScope = rememberCoroutineScope()
        // let's create a modifier state to specify how to update our UI state defined above
        val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            // note: scale goes by factor, not an absolute difference, so we need to multiply it
            // for this example, we don't allow downscaling, so cap it to 1f
            scale = max(scale * zoomChange, 1f)
            rotation += rotationChange
            offset += offsetChange
        }
        Box(
            Modifier
                // apply pan offset state as a layout transformation before other modifiers
                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                // add transformable to listen to multitouch transformation events after offset
                .transformable(state = state)
                // optional for example: add double click to zoom
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            coroutineScope.launch { state.animateZoomBy(4f) }
                        }
                    )
                }
                .fillMaxSize()
                .border(1.dp, Color.Green),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "\uD83C\uDF55",
                fontSize = 32.sp,
                // apply other transformations like rotation and zoom on the pizza slice emoji
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    rotationZ = rotation
                }
            )
        }
    }
}

@Composable
fun SnapFlingBehaviorSnapPosition() {
    val state = rememberLazyListState()
    val density = LocalDensity.current

    // Illustrate using a custom SnapPosition that will snap to a static location (200dp) after
    // the content padding.
    val snappingLayout = remember(state, density) {
        val snapPosition = object : SnapPosition {
            override fun position(
                layoutSize: Int,
                itemSize: Int,
                beforeContentPadding: Int,
                afterContentPadding: Int,
                itemIndex: Int,
                itemCount: Int,
            ): Int {
                return with(density) { beforeContentPadding + 200.dp.roundToPx() }
            }
        }
        SnapLayoutInfoProvider(state, snapPosition)
    }
    val flingBehavior = rememberSnapFlingBehavior(snappingLayout)

    LazyRow(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        state = state,
        flingBehavior = flingBehavior
    ) {
        items(200) {
            Box(
                modifier = Modifier
                    .height(400.dp)
                    .width(200.dp)
                    .padding(8.dp)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(it.toString(), fontSize = 32.sp)
            }
        }
    }
}

@Composable
fun AndroidExternalSurfaceColors() {
    AndroidExternalSurface(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        // Resources can be initialized/cached here

        // A surface is available, we can start rendering
        onSurface { surface, width, height ->
            var w = width
            var h = height

            // Initial draw to avoid a black frame
            surface.lockCanvas(Rect(0, 0, w, h)).apply {
                drawColor(Color.Blue.toArgb())
                surface.unlockCanvasAndPost(this)
            }

            // React to surface dimension changes
            surface.onChanged { newWidth, newHeight ->
                w = newWidth
                h = newHeight
            }

            // Cleanup if needed
            surface.onDestroyed {
            }

            // Render loop, automatically cancelled on surface destruction
            while (true) {
                withFrameNanos { time ->
                    surface.lockCanvas(Rect(0, 0, w, h)).apply {
                        val timeMs = time / 1_000_000L
                        val t = 0.5f + 0.5f * sin(timeMs / 1_000.0f)
                        drawColor(lerp(Color.Magenta, Color.Green, t).toArgb())
                        surface.unlockCanvasAndPost(this)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OverscrollSample() {
    @OptIn(ExperimentalFoundationApi::class)
    // our custom offset overscroll that offset the element it is applied to when we hit the bound
    // on the scrollable container.
    class OffsetOverscrollEffect(val scope: CoroutineScope) : OverscrollEffect {
        private val overscrollOffset = Animatable(0f)

        override fun applyToScroll(
            delta: Offset,
            source: NestedScrollSource,
            performScroll: (Offset) -> Offset,
        ): Offset {
            // in pre scroll we relax the overscroll if needed
            // relaxation: when we are in progress of the overscroll and user scrolls in the
            // different direction = substract the overscroll first
            val sameDirection = sign(delta.y) == sign(overscrollOffset.value)
            val consumedByPreScroll = if (abs(overscrollOffset.value) > 0.5 && !sameDirection) {
                val prevOverscrollValue = overscrollOffset.value
                val newOverscrollValue = overscrollOffset.value + delta.y
                if (sign(prevOverscrollValue) != sign(newOverscrollValue)) {
                    // sign changed, coerce to start scrolling and exit
                    scope.launch { overscrollOffset.snapTo(0f) }
                    Offset(x = 0f, y = delta.y + prevOverscrollValue)
                } else {
                    scope.launch {
                        overscrollOffset.snapTo(overscrollOffset.value + delta.y)
                    }
                    delta.copy(x = 0f)
                }
            } else {
                Offset.Zero
            }
            val leftForScroll = delta - consumedByPreScroll
            val consumedByScroll = performScroll(leftForScroll)
            val overscrollDelta = leftForScroll - consumedByScroll
            // if it is a drag, not a fling, add the delta left to our over scroll value
            if (abs(overscrollDelta.y) > 0.5 && source == NestedScrollSource.Drag) {
                scope.launch {
                    // multiply by 0.1 for the sake of parallax effect
                    overscrollOffset.snapTo(overscrollOffset.value + overscrollDelta.y * 0.1f)
                }
            }
            return consumedByPreScroll + consumedByScroll
        }

        override suspend fun applyToFling(
            velocity: Velocity,
            performFling: suspend (Velocity) -> Velocity,
        ) {
            val consumed = performFling(velocity)
            // when the fling happens - we just gradually animate our overscroll to 0
            val remaining = velocity - consumed
            overscrollOffset.animateTo(
                targetValue = 0f,
                initialVelocity = remaining.y,
                animationSpec = spring()
            )
        }

        override val isInProgress: Boolean
            get() = overscrollOffset.value != 0f

        // as we're building an offset modifiers, let's offset of our value we calculated
        override val effectModifier: Modifier = Modifier.offset {
            IntOffset(x = 0, y = overscrollOffset.value.roundToInt())
        }
    }

    val offset = remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()
    // Create the overscroll controller
    val overscroll = remember(scope) { OffsetOverscrollEffect(scope) }
    // let's build a scrollable that scroll until -512 to 512
    val scrollStateRange = (-512f).rangeTo(512f)
    Box(
        Modifier
            .size(150.dp)
            .scrollable(
                orientation = Orientation.Vertical,
                state = rememberScrollableState { delta ->
                    // use the scroll data and indicate how much this element consumed.
                    val oldValue = offset.value
                    // coerce to our range
                    offset.value = (offset.value + delta).coerceIn(scrollStateRange)

                    offset.value - oldValue // indicate that we consumed what's needed
                },
                // pass the overscroll to the scrollable so the data is updated
                overscrollEffect = overscroll
            )
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            offset.value.roundToInt().toString(),
            style = TextStyle(fontSize = 32.sp),
            modifier = Modifier
                // show the overscroll only on the text, not the containers (just for fun)
                .overscroll(overscroll)
        )
    }
}