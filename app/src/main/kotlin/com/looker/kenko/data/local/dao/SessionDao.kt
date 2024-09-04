package com.looker.kenko.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.looker.kenko.data.local.model.SessionEntity
import com.looker.kenko.data.local.model.SetEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface SessionDao {

    @Upsert
    suspend fun upsert(session: SessionEntity)

    @Query("SELECT EXISTS(SELECT * FROM sessions WHERE date = :date)")
    suspend fun wasCreatedOn(date: LocalDate): Boolean

    @Query("UPDATE sessions SET sets = :sets WHERE date = :date")
    suspend fun updateSets(date: LocalDate, sets: List<SetEntity>)

    @Query("SELECT * FROM sessions")
    fun stream(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE date = :date")
    fun session(date: LocalDate): Flow<SessionEntity?>

    @Query("SELECT * FROM sessions WHERE date = :date")
    suspend fun getSession(date: LocalDate): SessionEntity?

}