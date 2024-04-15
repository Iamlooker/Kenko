package com.looker.kenko.ui.getStarted

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath

private const val DEFAULT_VERTICES = 8
private const val ROTATION_DURATION_MILLIS = 32_000
private const val ROTATION_LIMIT = -360F
private const val JUMPING_DURATION_MILLIS = 2_000
private const val JUMPING_OFFSET = 64F

data class StarConfig(
    val jumpingOffset: Float = JUMPING_OFFSET,
    val jumpingDuration: Int = JUMPING_DURATION_MILLIS,
    val vertices: Int = DEFAULT_VERTICES,
    val rotationDuration: Int = ROTATION_DURATION_MILLIS,
    val rotationLimit: Float = ROTATION_LIMIT,
    val radiusRatio: Float = 1 / 2F,
    val innerRadiusRatio: Float = 0.8F,
    val cornerRatio: Float = 0.17F,
    val borderWidth: Float = 8F,
)

@Composable
fun Star(
    modifier: Modifier = Modifier,
    config: StarConfig = StarConfig(),
    color: Color = MaterialTheme.colorScheme.secondaryContainer,
    borderColor: Color = MaterialTheme.colorScheme.secondary,
) {
    val transition = rememberInfiniteTransition(label = "")
    val rotation by transition.animateFloat(
        initialValue = 0F,
        targetValue = config.rotationLimit,
        animationSpec = infiniteRepeatable(
            animation = tween(
                config.rotationDuration,
                easing = LinearEasing
            )
        ),
        label = "Rotation"
    )
    val jumping by transition.animateFloat(
        initialValue = 0F,
        targetValue = config.jumpingOffset,
        animationSpec = infiniteRepeatable(
            animation = tween(
                config.jumpingDuration,
                easing = FastOutLinearInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Jumping"
    )
    Box(
        modifier = modifier
            .drawWithCache {
                val width = size.width
                val star = RoundedPolygon.star(
                    numVerticesPerRadius = config.vertices,
                    radius = width * config.radiusRatio,
                    innerRadius = width * config.innerRadiusRatio * config.radiusRatio,
                    rounding = CornerRounding(width * config.cornerRatio),
                    innerRounding = CornerRounding(width * config.cornerRatio),
                    centerX = width / 2,
                    centerY = width / 2
                )
                val path = star
                    .toPath()
                    .asComposePath()
                onDrawBehind {
                    withTransform(
                        transformBlock = {
                            translate(top = jumping)
                            rotate(rotation)
                        }
                    ) {
                        drawPath(path = path, color = color)
                        drawPath(
                            path = path,
                            color = borderColor,
                            style = Stroke(width = config.borderWidth)
                        )
                    }
                }
            }
    )
}