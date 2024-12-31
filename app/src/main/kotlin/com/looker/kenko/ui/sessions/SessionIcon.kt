package com.looker.kenko.ui.sessions

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.circle
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import kotlinx.datetime.DayOfWeek
import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.SUNDAY
import java.time.DayOfWeek.THURSDAY
import java.time.DayOfWeek.TUESDAY
import java.time.DayOfWeek.WEDNESDAY

@Composable
fun SessionIcon(
    dayOfWeek: DayOfWeek,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondaryContainer,
    borderColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
) {
    val vertices = remember(dayOfWeek) {
        when (dayOfWeek) {
            MONDAY -> 3
            TUESDAY -> 4
            WEDNESDAY -> 5
            THURSDAY -> 6
            FRIDAY -> 7
            SATURDAY -> 6
            SUNDAY -> null
        }
    }
    Box(
        modifier = modifier
            .drawWithCache {
                val path = Path()
                val width = size.width
                val height = size.height
                if (vertices != null) {
                    val star = RoundedPolygon.star(
                        numVerticesPerRadius = vertices,
                        radius = width / 2,
                        innerRadius = width * 0.7F / 2,
                        rounding = CornerRounding(width * 0.12F),
                        centerX = width,
                        centerY = height / 2
                    )
                    path.addPath(star.toPath().asComposePath())
                } else {
                    val circle = RoundedPolygon.circle(
                        radius = width / 2.3F,
                        centerX = width,
                        centerY = height / 2
                    )
                    path.addPath(circle.toPath().asComposePath())
                }
                onDrawBehind {
                    drawPath(path = path, color = color)
                    // Border
                    drawPath(
                        path = path,
                        color = borderColor,
                        style = Stroke(width = 4F)
                    )
                }
            }
    )
}
