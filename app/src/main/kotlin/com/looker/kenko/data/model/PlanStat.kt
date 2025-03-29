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

package com.looker.kenko.data.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.util.packInts
import androidx.compose.ui.util.unpackInt1
import androidx.compose.ui.util.unpackInt2

@Immutable
@JvmInline
value class PlanStat(private val packedInt: Long) {

    @Stable
    val exercises: Int get() = unpackInt1(packedInt)

    @Stable
    val workDays: Int get() = unpackInt2(packedInt)

    @Stable
    val restDays: Int get() = 7 - workDays
}

fun PlanStat(exercises: Int, workDays: Int): PlanStat {
    return PlanStat(packInts(exercises, workDays))
}

