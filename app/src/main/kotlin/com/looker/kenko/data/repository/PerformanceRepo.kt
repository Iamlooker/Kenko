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

package com.looker.kenko.data.repository

import androidx.compose.runtime.Immutable
import com.looker.kenko.data.model.Set
import kotlinx.coroutines.flow.Flow

interface PerformanceRepo {
    fun performance(exerciseId: Int?, planId: Int?): Flow<Performance?>
    suspend fun getPerformance(exerciseId: Int?, planId: Int?): Performance?
}

@Immutable
data class Performance(
    val days: IntArray,
    val ratings: FloatArray,
    val starting: Set,
    val current: Set,
)
