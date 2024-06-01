package com.looker.kenko.ui.components

import androidx.annotation.FloatRange
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.theme.KenkoTheme
import kotlin.math.PI
import kotlin.math.sin

private const val WAVE_AMPLITUDE = 12F
private const val WAVE_FREQUENCY = 0.055F

private const val INITIAL_PHASE = 0F
private const val FINAL_PHASE = (2 * PI).toFloat()

@Composable
fun Wave(
    modifier: Modifier = Modifier,
    strokeWidth: Float = 4F,
    amplitude: Float = WAVE_AMPLITUDE,
    frequency: Float = WAVE_FREQUENCY,
    color: Color = MaterialTheme.colorScheme.tertiary,
) {
    Canvas(modifier = modifier) {
        drawWave(
            strokeColor = color,
            strokeWidth = strokeWidth,
            amplitude = amplitude,
            frequency = frequency
        )
    }
}

@Composable
fun AnimatedWave(
    modifier: Modifier = Modifier,
    strokeWidth: Float = 4F,
    amplitude: Float = WAVE_AMPLITUDE,
    frequency: Float = WAVE_FREQUENCY,
    color: Color = MaterialTheme.colorScheme.tertiary,
    durationMillis: Int = 1_000,
) {
    val transition = rememberInfiniteTransition("Wave")
    val phase by transition.animateFloat(
        initialValue = INITIAL_PHASE,
        targetValue = FINAL_PHASE,
        animationSpec = infiniteRepeatable(tween(durationMillis, easing = LinearEasing)),
        label = "Phase"
    )

    Canvas(modifier = modifier) {
        drawWave(
            strokeColor = color,
            strokeWidth = strokeWidth,
            amplitude = amplitude,
            frequency = frequency,
            phase = phase
        )
    }
}

fun DrawScope.drawWave(
    strokeColor: Color,
    strokeWidth: Float = 4F,
    amplitude: Float = WAVE_AMPLITUDE,
    frequency: Float = WAVE_FREQUENCY,
    @FloatRange(INITIAL_PHASE.toDouble(), FINAL_PHASE.toDouble()) phase: Float = 0F,
) {
    val path = Path()
    val centerY = center.y
    for (x in 0..size.width.toInt()) {
        val y = amplitude * sin((frequency * x) + phase)
        if (x == 0) {
            path.moveTo(0F, centerY)
        }
        path.lineTo(x.toFloat(), y + centerY)
    }
    drawPath(
        path = path,
        color = strokeColor,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun WavePreview() {
    KenkoTheme {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Howz the Wave?")
            Wave(
                modifier = Modifier
                    .weight(1F)
                    .height(24.dp)
            )
        }
    }
}