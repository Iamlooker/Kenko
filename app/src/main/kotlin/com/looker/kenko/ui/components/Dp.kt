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

package com.looker.kenko.ui.components

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.packFloats
import androidx.compose.ui.util.unpackFloat1
import androidx.compose.ui.util.unpackFloat2

@Immutable
@JvmInline
value class DpRange(private val packedFloat: Long) {

    @Stable
    val start: Dp get() = Dp(unpackFloat1(packedFloat))

    @Stable
    val end: Dp get() = Dp(unpackFloat2(packedFloat))

}

operator fun Dp.rangeTo(other: Dp): DpRange = DpRange(packFloats(value, other.value))

context(density: Density)
operator fun DpRange.contains(other: Float): Boolean = with(density) {
    other > start.toPx() && other < end.toPx()
}

operator fun DpRange.contains(other: Dp): Boolean = other > start && other < end
