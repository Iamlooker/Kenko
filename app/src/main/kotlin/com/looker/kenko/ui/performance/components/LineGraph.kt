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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun LineGraph(
    xAxis: IntArray,
    yAxis: FloatArray,
    modifier: Modifier = Modifier,
) {
    val data = rememberPoints(points = yAxis) {}
    Spacer(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.large,
            )
            .padding(12.dp)
            .drawBehind {
                drawPath(data.toPath(size), color = data.lineColor, style = data.stroke)
            },
    )
}

@Preview
@Composable
private fun LineGraphPreview() {
    KenkoTheme {
        LineGraph(
            xAxis = intArrayOf(1, 2, 3, 4, 5),
            yAxis = floatArrayOf(22F, 13.7F, 31F, 20F, 22F),
            modifier = Modifier.size(400.dp, 300.dp),
        )
    }
}
