package com.looker.kenko.ui.components.texture

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape

val defaultTextureColor: Color
    @Composable
    get() = MaterialTheme.colorScheme.surfaceContainer

@Composable
fun TexturedSurface(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
    textureColor: Color = defaultTextureColor,
    shape: Shape = RectangleShape,
    content: @Composable () -> Unit,
) {
    Surface(
        color = color,
        shape = shape
    ) {
        Surface(
            modifier = Modifier
                .dottedTexture(textureColor, isForeground = false)
                .then(modifier),
            color = Color.Transparent,
            shape = shape,
            content = content
        )
    }
}

fun Modifier.dottedGradient(
    color: Color,
    drawRatio: Float = 0.3F,
    start: GradientStart = GradientStart.TopLeft,
): Modifier = drawWithCache {
    onDrawBehind {
        dottedTexture(color, drawDistanceRatio = drawRatio, start = start)
    }
}

fun Modifier.dottedTexture(
    textureColor: Color,
    isForeground: Boolean = true,
) = drawWithContent {
    if (isForeground) {
        drawContent()
        dottedTexture(textureColor, Float.POSITIVE_INFINITY)
    } else {
        dottedTexture(textureColor, Float.POSITIVE_INFINITY)
        drawContent()
    }
}
