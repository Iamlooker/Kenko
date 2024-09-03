package com.looker.kenko.data.repository.offline

import com.looker.kenko.data.local.dao.PlanDao
import com.looker.kenko.data.local.model.PlanEntity
import com.looker.kenko.data.local.model.toEntity
import com.looker.kenko.data.local.model.toExternal
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

    override val stream: Flow<List<Plan>> =
        dao.stream().map { it.map(PlanEntity::toExternal) }

    override val current: Flow<Plan?> =
        dao.currentPlanStream().map { it?.toExternal() }

    override fun get(id: Int?): Flow<Plan?> {
        return id?.let { dao.getStream(it).map { it?.toExternal() } } ?: flowOf(null)
    }

    override fun exercises(date: LocalDate): Flow<List<Exercise>?> {
        return current.map { it?.exercisesPerDay?.get(date.dayOfWeek) }
    }

    override suspend fun switchPlan(plan: Plan) {
        dao.switchPlan(plan.id!!)
    }

    override suspend fun upsert(plan: Plan) {
        val isPlanActivable = plan.isActive && plan.id != null
        val entity = plan.toEntity()
        if (isPlanActivable) {
            dao.upsert(entity)
            dao.switchPlan(plan.id!!)
        } else {
            dao.upsert(entity.copy(isActive = false))
        }
    }

    override suspend fun remove(id: Int) {
        dao.remove(id)
    }

    override suspend fun current(): Plan? {
        return dao.currentPlan()?.toExternal()
    }

}