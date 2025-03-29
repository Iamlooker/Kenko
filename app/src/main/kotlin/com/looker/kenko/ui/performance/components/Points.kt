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

package com.looker.kenko.ui.performance.components

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke

@Immutable
class Points(
    val points: FloatArray,
    val stroke: Stroke,
    val smoothen: Boolean,
    val pointColor: Color,
    val lineColor: Color,
    val gridColor: Color,
    val label: @Composable () -> Unit,
)

fun Points.toPath(size: Size): Path = Path().apply {
    if (points.isEmpty()) return@apply
    val stepSize = size.width / points.size
    val xScaleSize = size.height / (points.max() - points.min())
    moveTo(points[0], 0F)
    for (i in 1..points.lastIndex) {
        val x = points[i] * 20F
        val y = stepSize * i
        lineTo(x, y)
    }
}

private val defaultStroke = Stroke(
    width = 1F,
    cap = StrokeCap.Round,
    join = StrokeJoin.Round,
)

@Composable
fun rememberPoints(
    points: FloatArray,
    stroke: Stroke = defaultStroke,
    smoothen: Boolean = true,
    pointColor: Color = MaterialTheme.colorScheme.secondary,
    lineColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    gridColor: Color = MaterialTheme.colorScheme.outlineVariant,
    labelColor: Color = MaterialTheme.colorScheme.outline,
    label: @Composable () -> Unit,
): Points = Points(
    points = points,
    stroke = stroke,
    smoothen = smoothen,
    pointColor = pointColor,
    lineColor = lineColor,
    gridColor = gridColor,
    label = {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.labelMedium,
            LocalContentColor provides labelColor,
            content = label,
        )
    },
)
