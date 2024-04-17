package com.looker.kenko.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.looker.kenko.data.model.Plan
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

    @Query("SELECT * FROM plan_table")
    fun stream(): Flow<List<Plan>>

    @Query("SELECT * FROM plan_table WHERE isActive = :isActive")
    fun currentPlanStream(isActive: Boolean): Flow<Plan?>

    @Query("SELECT * FROM plan_table WHERE id = :id")
    fun getStream(id: Long): Flow<Plan?>

    @Upsert
    suspend fun upsert(plan: Plan)

    @Query("SELECT * FROM plan_table WHERE isActive = 1")
    suspend fun currentPlan(): Plan?

}