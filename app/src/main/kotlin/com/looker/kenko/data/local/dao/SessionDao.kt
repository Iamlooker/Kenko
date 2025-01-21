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
import androidx.room.Transaction
import com.looker.kenko.data.local.model.SessionDataEntity
import com.looker.kenko.data.local.model.SessionEntity
import com.looker.kenko.utils.EpochDays
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert
    suspend fun insert(session: SessionDataEntity)

    @Query(
        """
        SELECT EXISTS
        (SELECT *
        FROM sessions
        WHERE date = :date)
        """,
    )
    suspend fun sessionExistsOn(date: EpochDays): Boolean

    @Query(
        """
        SELECT date
        FROM sessions
        WHERE id = :sessionId
        """,
    )
    suspend fun getDatePerformedOn(sessionId: Int): EpochDays

    @Query(
        """
        SELECT COUNT(*)
        FROM sessions
        """,
    )
    suspend fun getTotalSessions(): Int

    @Query(
        """
        SELECT id
        FROM sessions
        WHERE date = :date
        """,
    )
    suspend fun getSessionId(date: EpochDays): Int?

    @Transaction
    @Query(
        """
        SELECT *
        FROM sessions
        """,
    )
    fun stream(): Flow<List<SessionEntity>>

    @Transaction
    @Query(
        """
        SELECT *
        FROM sessions
        WHERE date = :date
        """,
    )
    fun session(date: EpochDays): Flow<SessionEntity?>

    @Transaction
    @Query(
        """
        SELECT *
        FROM sessions
        WHERE date = :date
        """,
    )
    suspend fun getSession(date: EpochDays): SessionEntity?
}
