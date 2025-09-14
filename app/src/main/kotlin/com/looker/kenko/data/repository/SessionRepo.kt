/*
 * Copyright (C) 2025. LooKeR & Contributors
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

import com.looker.kenko.data.local.model.SetType
import com.looker.kenko.data.model.RIR
import com.looker.kenko.data.model.RPE
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.Set
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface SessionRepo {

    val stream: Flow<List<Session>>

    val setsCount: Flow<Int>

    suspend fun addSet(sessionId: Int, set: Set)

    suspend fun addSet(
        sessionId: Int,
        exerciseId: Int,
        weight: Float,
        reps: Int,
        setType: SetType,
        rir: RIR,
        rpe: RPE,
    )

    suspend fun removeSet(setId: Int)

    suspend fun getSessionIdOrCreate(date: LocalDate): Int

    fun streamByDate(date: LocalDate): Flow<Session?>

    suspend fun getSets(sessionId: Int): List<Set>
}
