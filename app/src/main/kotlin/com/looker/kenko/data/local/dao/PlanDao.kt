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
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import com.looker.kenko.data.local.model.ExerciseEntity
import com.looker.kenko.data.local.model.PlanDayEntity
import com.looker.kenko.data.local.model.PlanEntity
import com.looker.kenko.data.model.Labels
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
        (SELECT *
        FROM plans
        WHERE name = :planName)
        """,
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

    @Query(
        """
        SELECT *
        FROM plans
        WHERE id =
        (SELECT planId
        FROM plan_day
        WHERE exerciseId = :exerciseId)
        """,
    )
    suspend fun getPlanByExerciseId(exerciseId: Int): List<PlanEntity>

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

    suspend fun searchPlans(
        query: String? = null,
        difficulty: Labels.Difficulty? = null,
        focus: Labels.Focus? = null,
        equipment: Labels.Equipment? = null,
        time: Labels.Time? = null,
    ): List<PlanEntity> {
        val args = mutableListOf<Any?>()
        val sql = buildString(256) {
            append("SELECT * FROM plans WHERE 1=1 ")
            if (!query.isNullOrBlank()) {
                append("AND name LIKE %?% OR description LIKE %?% ")
                args.add(query)
                args.add(query)
            }
            if (difficulty != null) {
                append("AND difficulty = ? ")
                args.add(difficulty)
            }
            if (focus != null) {
                append("AND focus = ? ")
                args.add(focus)
            }
            if (equipment != null) {
                append("AND equipment = ? ")
                args.add(equipment)
            }
            if (time != null) {
                append("AND time = ? ")
                args.add(time)
            }
        }
        return _rawSearchPlans(
            SimpleSQLiteQuery(
                query = sql,
                bindArgs = args.toTypedArray(),
            ),
        )
    }

    @RawQuery
    suspend fun _rawSearchPlans(query: SimpleSQLiteQuery): List<PlanEntity>

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
