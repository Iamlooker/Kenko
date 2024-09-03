package com.looker.kenko.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.looker.kenko.data.local.model.PlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

    @Query("SELECT * FROM plans")
    fun stream(): Flow<List<PlanEntity>>

    @Query("SELECT * FROM plans WHERE isActive = 1")
    fun currentPlanStream(): Flow<PlanEntity?>

    @Query("UPDATE plans SET isActive = 0 WHERE isActive = 1")
    suspend fun deactivateOldPlan()

    @Query("UPDATE plans SET isActive = 1 WHERE id = :id")
    suspend fun activatePlan(id: Long)

    @Transaction
    suspend fun switchPlan(id: Long) {
        deactivateOldPlan()
        activatePlan(id)
    }

    @Query("SELECT * FROM plans WHERE id = :id")
    fun getStream(id: Long): Flow<PlanEntity?>

    @Upsert
    suspend fun upsert(plan: PlanEntity)

    @Query("DELETE FROM plans WHERE id = :id")
    suspend fun remove(id: Long)

    @Query("SELECT * FROM plans WHERE isActive = 1")
    suspend fun currentPlan(): PlanEntity?

}