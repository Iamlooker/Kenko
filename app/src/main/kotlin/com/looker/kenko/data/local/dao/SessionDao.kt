package com.looker.kenko.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.looker.kenko.data.local.model.SessionDataEntity
import com.looker.kenko.data.local.model.SessionEntity
import com.looker.kenko.data.local.model.SetEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface SessionDao {

    @Upsert
    suspend fun upsert(session: SessionDataEntity)

    @Query(
        """
        SELECT EXISTS
        (SELECT *
        FROM sessions
        WHERE date = :date)
        """
    )
    suspend fun sessionExistsOn(date: LocalDate): Boolean

    @Upsert
    suspend fun addSet(set: SetEntity)

    @Delete
    suspend fun removeSet(set: SetEntity)

    @Query(
        """
        SELECT *
        FROM sets
        WHERE sessionId = :sessionId
        ORDER BY `order`
        """
    )
    suspend fun getSets(sessionId: Int): List<SetEntity>

    @Query(
        """
        SELECT COUNT(*)
        FROM sets
        WHERE sessionId = :sessionId
        """
    )
    suspend fun getSetCount(sessionId: Int): Int

    @Query(
        """
        SELECT id
        FROM sessions
        WHERE date = :date
        """
    )
    suspend fun getSessionId(date: LocalDate): Int?

    @Transaction
    @Query(
        """
        SELECT *
        FROM sessions
        """
    )
    fun stream(): Flow<List<SessionEntity>>

    @Transaction
    @Query(
        """
        SELECT *
        FROM sessions
        WHERE date = :date
        """
    )
    fun session(date: LocalDate): Flow<SessionEntity?>

    @Transaction
    @Query(
        """
        SELECT *
        FROM sessions
        WHERE date = :date
        """
    )
    suspend fun getSession(date: LocalDate): SessionEntity?
}
