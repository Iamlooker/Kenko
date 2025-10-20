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
import androidx.room.Query
import androidx.room.Upsert
import com.looker.kenko.data.local.model.PlanHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanHistoryDao {

    @Query(
        """
        SELECT planId
        FROM plan_history
        WHERE `end` IS NULL
        AND start IS NOT NULL
        """,
    )
    fun currentIdFlow(): Flow<Int?>

    @Query(
        """
        SELECT planId
        FROM plan_history
        WHERE `end` IS NULL
        AND start IS NOT NULL
        """,
    )
    suspend fun getCurrentId(): Int?

    @Query(
        """
        SELECT *
        FROM plan_history
        WHERE `end` IS NULL
        AND start IS NOT NULL
        """,
    )
    fun currentFlow(): Flow<PlanHistoryEntity?>

    @Query(
        """
        SELECT *
        FROM plan_history
        WHERE `end` IS NULL
        AND start IS NOT NULL
        """,
    )
    suspend fun getCurrent(): PlanHistoryEntity?

    @Query(
        """
        SELECT *
        FROM plan_history
        """
    )
    suspend fun getAll(): List<PlanHistoryEntity>

    @Upsert
    suspend fun upsert(history: PlanHistoryEntity)
}
