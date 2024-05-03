package com.looker.kenko.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.looker.kenko.data.model.Plan
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

    @Query("SELECT * FROM plan_table")
    fun stream(): Flow<List<Plan>>

    @Query("SELECT * FROM plan_table WHERE isActive = :isActive")
    fun currentPlanStream(isActive: Boolean): Flow<Plan?>

    @Query("UPDATE plan_table SET isActive = 0 WHERE isActive = 1")
    suspend fun deactivateOldPlan()

    @Query("UPDATE plan_table SET isActive = 1 WHERE id = :id")
    suspend fun activatePlan(id: Long)

    @Transaction
    suspend fun switchPlan(id: Long) {
        deactivateOldPlan()
        activatePlan(id)
    }

    @Query("SELECT * FROM plan_table WHERE id = :id")
    fun getStream(id: Long): Flow<Plan?>

    @Upsert
    suspend fun upsert(plan: Plan)

    @Query("SELECT * FROM plan_table WHERE isActive = 1")
    suspend fun currentPlan(): Plan?

}