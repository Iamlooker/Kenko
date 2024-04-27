package com.looker.kenko.data.repository

import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Plan
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface PlanRepo {

    val stream: Flow<List<Plan>>

    val current: Flow<Plan?>

    fun get(id: Long?): Flow<Plan?>

    fun exercises(date: LocalDate): Flow<List<Exercise>?>

    suspend fun upsert(plan: Plan)

    suspend fun current(): Plan?

}