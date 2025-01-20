package com.looker.kenko.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
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
        WHERE exerciseId = :exerciseId
        ORDER BY `order`
        """,
    )
    fun setsByExerciseId(exerciseId: Int): Flow<List<SetEntity>>

    @Query(
        """
        SELECT *
        FROM sets
        WHERE exerciseId = :exerciseId
        ORDER BY `order`
        """,
    )
    suspend fun getSetsByExerciseId(exerciseId: Int): List<SetEntity>

    @Query(
        """
        SELECT *
        FROM sets
        WHERE exerciseId = :exerciseId
        AND sessionId IN (
            SELECT id
            FROM sessions
            WHERE planId = :planId
        )
        """,
    )
    fun setsByExerciseIdPerPlan(exerciseId: Int, planId: Int): Flow<List<SetEntity>>

    @Query(
        """
        SELECT *
        FROM sets
        WHERE exerciseId = :exerciseId
        AND sessionId IN (
            SELECT id
            FROM sessions
            WHERE planId = :planId
        )
        """,
    )
    suspend fun getSetsByExerciseIdPerPlan(exerciseId: Int, planId: Int): List<SetEntity>

    @Query(
        """
        SELECT *
        FROM sets
        WHERE sessionId IN (
            SELECT id
            FROM sessions
            WHERE planId = :planId
        )
        """,
    )
    fun setsByPlanId(planId: Int): Flow<List<SetEntity>>

    @Query(
        """
        SELECT *
        FROM sets
        WHERE sessionId IN (
            SELECT id
            FROM sessions
            WHERE planId = :planId
        )
        """,
    )
    suspend fun getSetsByPlanId(planId: Int): List<SetEntity>

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
        """
    )
    suspend fun delete(setId: Int)
}
