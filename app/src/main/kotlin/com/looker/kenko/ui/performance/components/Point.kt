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

@file:Suppress("NOTHING_TO_INLINE")

package com.looker.kenko.ui.performance.components

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.util.packFloats
import androidx.compose.ui.util.unpackFloat1
import androidx.compose.ui.util.unpackFloat2

@Stable
fun Point(x: Float, y: Float) = Point(packFloats(x, y))

@Immutable
@JvmInline
value class Point(val packedValue: Long) {

    @Stable
    val x: Float get() = unpackFloat1(packedValue)

    @Stable
    val y: Float get() = unpackFloat2(packedValue)

    @Stable
    inline operator fun component1(): Float = x

    @Stable
    inline operator fun component2(): Float = y

    override fun toString(): String = "Point: ($x, $y)"

}

@Stable
fun Offset.toCartesian(size: Size): Point = Point(x = x, y = size.height - y)

@Stable
fun Point.toOffset(size: Size): Offset = Offset(x = x, y = size.height - y)
