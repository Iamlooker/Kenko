package com.looker.kenko.data.repository.offline

import com.looker.kenko.data.local.dao.PlanDao
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.repository.PlanRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class OfflinePlanRepo @Inject constructor(
    private val dao: PlanDao,
) : PlanRepo {

    override val stream: Flow<List<Plan>>
        get() = dao.stream()

    override val current: Flow<Plan?>
        get() = dao.currentPlanStream()

    override fun get(id: Long?): Flow<Plan?> {
        return id?.let { dao.getStream(it) } ?: flowOf(null)
    }

    override fun exercises(date: LocalDate): Flow<List<Exercise>?> {
        return current.map { it?.exercisesPerDay?.get(date.dayOfWeek) }
    }

    override suspend fun switchPlan(plan: Plan) {
        dao.switchPlan(plan.id!!)
    }

    override suspend fun upsert(plan: Plan) {
        val isPlanActivable = plan.isActive && plan.id != null
        if (isPlanActivable) {
            dao.upsert(plan)
            dao.switchPlan(plan.id!!)
        } else {
            dao.upsert(plan.copy(isActive = false))
        }
    }

    override suspend fun remove(id: Long) {
        dao.remove(id)
    }

    override suspend fun current(): Plan? {
        return dao.currentPlan()
    }

}