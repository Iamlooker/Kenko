package com.looker.kenko.data.repository

import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.model.PlanItem
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DayOfWeek

interface PlanRepo {

    val plans: Flow<List<Plan>>

    val current: Flow<Plan?>

    val planItems: Flow<List<PlanItem>>

    fun planItems(id: Int): Flow<List<PlanItem>>

    fun planItems(id: Int, day: DayOfWeek): Flow<List<PlanItem>>

    fun activeExercises(day: DayOfWeek): Flow<List<Exercise>>

    suspend fun plan(id: Int): Plan?

    suspend fun planNameExists(name: String): Boolean

    suspend fun getPlanItems(id: Int): List<PlanItem>

    suspend fun getPlanItems(id: Int, day: DayOfWeek): List<PlanItem>

    suspend fun createPlan(name: String): Int

    suspend fun updatePlan(plan: Plan)

    suspend fun setCurrent(id: Int)

    suspend fun deletePlan(id: Int)

    suspend fun addItem(planItem: PlanItem)

    suspend fun removeItem(id: Long)

    suspend fun removeItemById(exerciseId: Int)

}
