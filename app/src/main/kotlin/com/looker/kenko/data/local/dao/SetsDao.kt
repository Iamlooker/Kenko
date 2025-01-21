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

package com.looker.kenko.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.looker.kenko.data.local.model.SetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SetsDao {

    @Query(
        """
        SELECT *
        FROM sets
        WHERE sessionId = :sessionId
        ORDER BY `order`
        """,
    )
    fun setsBySessionId(sessionId: Int): Flow<List<SetEntity>>

    @Query(
        """
        SELECT *
        FROM sets
        WHERE sessionId = :sessionId
        ORDER BY `order`
        """,
    )
    suspend fun getSetsBySessionId(sessionId: Int): List<SetEntity>

    @Query(
        """
        SELECT COUNT(*)
        FROM sets
        WHERE sessionId = :sessionId
        """,
    )
    suspend fun getSetsCountBySessionId(sessionId: Int): Int?

    @Query(
        """
        SELECT *
        FROM sets
        WHERE (:exerciseId IS NULL OR exerciseId = :exerciseId)
        AND sessionId IN (
            SELECT id
            FROM sessions
            WHERE (:planId IS NULL OR planId = :planId)
        )
        ORDER BY `order`
        """,
    )
    fun setsByExerciseIdPerPlan(exerciseId: Int? = null, planId: Int? = null): Flow<List<SetEntity>>

    @Query(
        """
        SELECT *
        FROM sets
        WHERE (:exerciseId IS NULL OR exerciseId = :exerciseId)
        AND sessionId IN (
            SELECT id
            FROM sessions
            WHERE (:planId IS NULL OR planId = :planId)
        )
        ORDER BY `order`
        """,
    )
    suspend fun getSetsByExerciseIdPerPlan(
        exerciseId: Int? = null,
        planId: Int? = null,
    ): List<SetEntity>

    @Query(
        """
        SELECT COUNT (*)
        FROM sets
        """,
    )
    fun totalSetCount(): Flow<Int>

    @Insert
    suspend fun insert(set: SetEntity)

    @Query(
        """
        DELETE
        FROM sets
        WHERE id = :setId
        """,
    )
    suspend fun delete(setId: Int)
}
