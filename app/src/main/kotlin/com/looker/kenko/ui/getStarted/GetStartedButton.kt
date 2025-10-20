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

package com.looker.kenko.ui.getStarted

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.theme.KenkoIcons

@Composable
fun ButtonGroup(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Layout(
        modifier = modifier,
        content = {
            Icon(
                modifier = Modifier.Companion.layoutId(ButtonID.Cloud),
                imageVector = KenkoIcons.Cloud,
                contentDescription = null,
            )
            Icon(
                modifier = Modifier.Companion.layoutId(ButtonID.Arrow1),
                imageVector = KenkoIcons.Arrow1,
                contentDescription = null,
            )
            Icon(
                modifier = Modifier.Companion.layoutId(ButtonID.Arrow2),
                imageVector = KenkoIcons.Arrow2,
                contentDescription = null,
            )
            Icon(
                modifier = Modifier.Companion.layoutId(ButtonID.Arrow3),
                imageVector = KenkoIcons.Arrow3,
                contentDescription = null,
            )
            Icon(
                modifier = Modifier.Companion.layoutId(ButtonID.Arrow4),
                imageVector = KenkoIcons.Arrow4,
                contentDescription = null,
            )
            Box(modifier = Modifier.layoutId(ButtonID.Button)) {
                content()
            }
        },
    ) { measurables, constraints ->
        lateinit var cloud: Measurable
        lateinit var arrow1: Measurable
        lateinit var arrow2: Measurable
        lateinit var arrow3: Measurable
        lateinit var arrow4: Measurable
        lateinit var button: Measurable
        measurables.forEach { measurable ->
            when (measurable.layoutId) {
                ButtonID.Button -> button = measurable
                ButtonID.Cloud -> cloud = measurable
                ButtonID.Arrow1 -> arrow1 = measurable
                ButtonID.Arrow2 -> arrow2 = measurable
                ButtonID.Arrow3 -> arrow3 = measurable
                ButtonID.Arrow4 -> arrow4 = measurable
                else -> error("Unknown Element")
            }
        }
        val cloudPlaceable = cloud.measure(constraints)
        val arrow1Placeable = arrow1.measure(constraints)
        val arrow2Placeable = arrow2.measure(constraints)
        val arrow3Placeable = arrow3.measure(constraints)
        val arrow4Placeable = arrow4.measure(constraints)
        val buttonPlaceable = button.measure(constraints)
        val width = 360.dp.toPx().toInt()
        layout(width, 162.dp.toPx().toInt()) {
            val x = (width / 2) - (buttonPlaceable.width / 2)
            cloudPlaceable.placeRelative(16.dp.toPx().toInt(), 31.dp.toPx().toInt())
            arrow1Placeable.placeRelative(148.dp.toPx().toInt(), 2.dp.toPx().toInt())
            arrow2Placeable.placeRelative(200.dp.toPx().toInt(), 0.dp.toPx().toInt())
            arrow3Placeable.placeRelative(270.dp.toPx().toInt(), 26.dp.toPx().toInt())
            arrow4Placeable.placeRelative(290.dp.toPx().toInt(), 100.dp.toPx().toInt())
            buttonPlaceable.placeRelative(x, 68.dp.toPx().toInt())
        }
    }
}

private enum class ButtonID {
    Cloud, Arrow1, Arrow2, Arrow3, Arrow4, Button
}
