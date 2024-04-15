package com.looker.kenko.data.repository.offline

import com.looker.kenko.data.local.dao.PlanDao
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.repository.PlanRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflinePlanRepo @Inject constructor (
    private val dao: PlanDao,
) : PlanRepo {

    override val stream: Flow<List<Plan>>
        get() = dao.stream()

    override val current: Flow<Plan?>
        get() = dao.currentPlanStream(true)

    override suspend fun upsert(plan: Plan) {
        dao.upsert(plan)
    }

    override suspend fun current(): Plan? {
        return dao.currentPlan()
    }

}