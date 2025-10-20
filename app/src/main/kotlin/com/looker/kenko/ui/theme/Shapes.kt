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

package com.looker.kenko.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

fun CornerBasedShape.end(
    bottomEnd: Dp = 0.dp,
    topEnd: Dp = bottomEnd,
): CornerBasedShape =
    copy(bottomEnd = CornerSize(bottomEnd), topEnd = CornerSize(topEnd))

fun CornerBasedShape.start(
    bottomStart: Dp = 0.dp,
    topStart: Dp = bottomStart,
): CornerBasedShape =
    copy(bottomStart = CornerSize(bottomStart), topStart = CornerSize(topStart))

fun CornerBasedShape.end(
    end: CornerBasedShape,
): CornerBasedShape =
    copy(bottomEnd = end.bottomEnd, topEnd = end.topEnd)

fun CornerBasedShape.start(
    start: CornerBasedShape,
): CornerBasedShape =
    copy(bottomStart = start.bottomStart, topStart = start.topStart)
