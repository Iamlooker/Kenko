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
