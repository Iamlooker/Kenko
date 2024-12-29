package com.looker.kenko.ui.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.colorSchemes.sereneColorSchemes

// TODO: This can take some noise ;)
@Composable
fun Star(
    modifier: Modifier = Modifier,
    brush: Brush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.tertiaryContainer,
        )
    ),
    border: BorderStroke? = OnSurfaceBorder,
) {
    Box(
        modifier = modifier
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .drawWithCache {
                val polygon = RoundedPolygon
                    .star(
                        numVerticesPerRadius = 4,
                        radius = size.width / 2F,
                        innerRadius = size.width / 4F,
                        innerRounding = CornerRounding(size.width, smoothing = 0.5F),
                        centerX = size.width / 2,
                        centerY = size.height / 2,
                    )
                    .toPath()
                    .asComposePath()
                onDrawBehind {
                    if (border != null) {
                        drawOutline(
                            outline = Outline.Generic(polygon),
                            brush = border.brush,
                            style = Stroke(width = border.width.toPx()),
                        )
                    }
                    drawPath(
                        path = polygon,
                        brush = brush,
                    )
                }
            }
    )
}

@Preview(showBackground = true)
@Composable
private fun AnimationPreview() {
    var target by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = target, label = "Rotation")
    val rotation by transition.animateFloat(
        label = "Rotation",
        transitionSpec = { tween(500) }
    ) { if (it) -90F else 0F }
    val translation by transition.animateIntOffset(
        label = "Translation",
        transitionSpec = { tween(500) }
    ) {
        if (it) {
            IntOffset(0, 0)
        } else {
            IntOffset(-10, 10)
        }
    }
    val scale by transition.animateFloat(
        label = "Scale",
        transitionSpec = { tween(500) }
    ) { if (it) 1F else 0.5F }
    KenkoTheme(colorSchemes = sereneColorSchemes) {
        Star(
            modifier = Modifier
                .size(32.dp)
                .offset { translation }
                .graphicsLayer {
                    rotationZ = rotation
                    scaleX = scale
                    scaleY = scale
                },
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSurface),
        )
    }
}
