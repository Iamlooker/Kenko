package com.looker.kenko.data.repository

import com.looker.kenko.data.model.Plan
import kotlinx.coroutines.flow.Flow

interface PlanRepo {

    val stream: Flow<List<Plan>>

    val current: Flow<Plan?>

    suspend fun upsert(plan: Plan)

    suspend fun current(): Plan?

}