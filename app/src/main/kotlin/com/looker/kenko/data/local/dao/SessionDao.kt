package com.looker.kenko.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.looker.kenko.data.local.model.SessionDataEntity
import com.looker.kenko.data.local.model.SessionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

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
    suspend fun sessionExistsOn(date: LocalDate): Boolean

    @Query(
        """
        SELECT date
        FROM sessions
        WHERE id = :sessionId
        """,
    )
    suspend fun getDatePerformedOn(sessionId: Int): LocalDate

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
    suspend fun getSessionId(date: LocalDate): Int?

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
    fun session(date: LocalDate): Flow<SessionEntity?>

    @Transaction
    @Query(
        """
        SELECT *
        FROM sessions
        WHERE date = :date
        """,
    )
    suspend fun getSession(date: LocalDate): SessionEntity?
}
