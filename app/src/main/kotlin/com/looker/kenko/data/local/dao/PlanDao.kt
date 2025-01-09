package com.looker.kenko.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.looker.kenko.data.local.model.ExerciseEntity
import com.looker.kenko.data.local.model.PlanDayEntity
import com.looker.kenko.data.local.model.PlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

    @Query(
        """
        SELECT *
        FROM plans
        """,
    )
    fun plansFlow(): Flow<List<PlanEntity>>

    @Transaction
    @Query(
        """
        SELECT *
        FROM plan_day
        WHERE planId =
        (SELECT planId
        FROM plan_history
        WHERE `end` IS NULL
        AND start IS NOT NULL)
        ORDER BY id ASC
        """,
    )
    fun currentPlanItemsFlow(): Flow<List<PlanDayEntity>>

    @Transaction
    @Query(
        """
        SELECT *
        FROM plan_day
        WHERE planId =
        (SELECT planId
        FROM plan_history
        WHERE `end` IS NULL
        AND start IS NOT NULL)
        AND dayOfWeek = :day
        ORDER BY id ASC
        """,
    )
    fun currentPlanItemsByDayFlow(day: Int): Flow<List<PlanDayEntity>>

    @Query(
        """
        SELECT *
        FROM plans
        WHERE id = :planId
        """,
    )
    suspend fun getPlanById(planId: Int): PlanEntity?

    @Query(
        """
        SELECT EXISTS
        (SELECT name
        FROM plans
        WHERE name = :planName)
        """
    )
    suspend fun exists(planName: String): Boolean

    @Query(
        """
        SELECT *
        FROM plan_day
        WHERE planId = :planId
        ORDER BY id ASC
        """,
    )
    fun planItemsByPlanIdFlow(planId: Int): Flow<List<PlanDayEntity>>

    @Query(
        """
        SELECT *
        FROM plan_day
        WHERE planId = :planId
        ORDER BY id ASC
        """,
    )
    suspend fun getPlanItemsByPlanId(planId: Int): List<PlanDayEntity>

    @Transaction
    @Query(
        """
        SELECT exercises.*
        FROM exercises
        INNER JOIN plan_day
        ON exercises.id = plan_day.exerciseId
        WHERE plan_day.planId = :planId
        ORDER BY plan_day.id ASC
        """,
    )
    fun exerciseByPlanIdFlow(planId: Int): Flow<List<ExerciseEntity>>

    @Transaction
    @Query(
        """
        SELECT exercises.*
        FROM exercises
        INNER JOIN plan_day
        ON exercises.id = plan_day.exerciseId
        WHERE plan_day.planId = :planId
        ORDER BY plan_day.id ASC
        """,
    )
    suspend fun getExerciseByPlanId(planId: Int): List<ExerciseEntity>

    @Query(
        """
        SELECT *
        FROM plan_day
        WHERE planId = :planId
        AND dayOfWeek = :day
        ORDER BY id ASC
        """,
    )
    fun planItemsByPlanIdAndDayFlow(planId: Int, day: Int): Flow<List<PlanDayEntity>>

    @Query(
        """
        SELECT *
        FROM plan_day
        WHERE planId = :planId
        AND dayOfWeek = :day
        ORDER BY id ASC
        """,
    )
    suspend fun getPlanItemsByPlanIdAndDay(planId: Int, day: Int): List<PlanDayEntity>

    @Query(
        """
        SELECT COUNT(exerciseId)
        FROM plan_day
        WHERE planId = :planId
        """,
    )
    suspend fun getExerciseCountByPlanId(planId: Int): Int

    @Query(
        """
        SELECT COUNT(DISTINCT dayOfWeek)
        FROM plan_day
        WHERE planId = :planId
        """,
    )
    suspend fun getWorkDaysByPlanId(planId: Int): Int

    @Upsert
    suspend fun upsertPlan(plan: PlanEntity): Long

    @Transaction
    @Query("DELETE FROM plans WHERE id = :planId")
    suspend fun deletePlan(planId: Int)

    @Upsert
    suspend fun insertPlanItem(item: PlanDayEntity)

    @Query("DELETE FROM plan_day WHERE id = :planDayId")
    suspend fun deleteItem(planDayId: Long)

    @Query("DELETE FROM plan_day WHERE exerciseId = :exerciseId")
    suspend fun deleteItemByExercise(exerciseId: Int)
}
