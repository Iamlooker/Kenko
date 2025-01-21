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

package com.looker.kenko.data.repository.local

import com.looker.kenko.data.local.dao.ExerciseDao
import com.looker.kenko.data.local.dao.PlanDao
import com.looker.kenko.data.local.dao.PlanHistoryDao
import com.looker.kenko.data.local.model.PlanEntity
import com.looker.kenko.data.local.model.PlanHistoryEntity
import com.looker.kenko.data.local.model.toEntity
import com.looker.kenko.data.local.model.toExternal
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.model.PlanItem
import com.looker.kenko.data.model.PlanStat
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.utils.toLocalEpochDays
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DayOfWeek
import javax.inject.Inject

class LocalPlanRepo @Inject constructor(
    private val dao: PlanDao,
    private val exerciseDao: ExerciseDao,
    private val historyDao: PlanHistoryDao,
) : PlanRepo {

    override val plans: Flow<List<Plan>> =
        combine(dao.plansFlow(), historyDao.currentIdFlow()) { plans, current ->
            plans.map {
                it.toExternal(isActive = it.id == current, stat = stats(it.id))
            }
        }

    override val current: Flow<Plan?> =
        historyDao.currentIdFlow().map { current ->
            if (current != null) {
                dao.getPlanById(current)?.toExternal(true, stats(current))
            } else {
                null
            }
        }

    override val planItems: Flow<List<PlanItem>> =
        dao.currentPlanItemsFlow().map { planDays ->
            planDays.map { planDay ->
                planDay.toExternal { exerciseId ->
                    exerciseDao.get(exerciseId)?.toExternal()
                }
            }
        }

    override suspend fun plan(id: Int): Plan? {
        val isCurrent = current.first()?.id
        return dao.getPlanById(id)?.toExternal(isCurrent == id, stats(id))
    }

    override suspend fun planNameExists(name: String): Boolean =
        dao.exists(name)

    override fun planItems(id: Int): Flow<List<PlanItem>> =
        dao.planItemsByPlanIdFlow(id).map {
            it.map { planDay ->
                planDay.toExternal { exerciseId ->
                    exerciseDao.get(exerciseId)?.toExternal()
                }
            }
        }

    override fun planItems(id: Int, day: DayOfWeek): Flow<List<PlanItem>> =
        dao.planItemsByPlanIdAndDayFlow(id, day.value).map {
            it.map { planDay ->
                planDay.toExternal { exerciseId ->
                    exerciseDao.get(exerciseId)?.toExternal()
                }
            }
        }

    override fun activeExercises(day: DayOfWeek): Flow<List<Exercise>> =
        dao.currentPlanItemsByDayFlow(day.value).map {
            it.mapNotNull { planDay ->
                exerciseDao.get(planDay.exerciseId)?.toExternal()
            }
        }

    override suspend fun getPlanItems(id: Int): List<PlanItem> =
        dao.getPlanItemsByPlanId(id).map {
            it.toExternal { exerciseId ->
                exerciseDao.get(exerciseId)?.toExternal()
            }
        }

    override suspend fun getPlanItems(id: Int, day: DayOfWeek): List<PlanItem> =
        dao.getPlanItemsByPlanIdAndDay(id, day.value).map {
            it.toExternal { exerciseId ->
                exerciseDao.get(exerciseId)?.toExternal()
            }
        }

    private suspend fun stats(id: Int): PlanStat =
        PlanStat(dao.getExerciseCountByPlanId(id), dao.getWorkDaysByPlanId(id))

    override suspend fun createPlan(name: String): Int =
        dao.upsertPlan(PlanEntity(name)).toInt()

    override suspend fun updatePlan(plan: Plan) {
        dao.upsertPlan(plan.toEntity())
    }

    override suspend fun setCurrent(id: Int) {
        val current = historyDao.getCurrent()
        if (current != null) {
            historyDao.upsert(current.copy(end = localDate.toLocalEpochDays()))
        }
        historyDao.upsert(PlanHistoryEntity(planId = id, start = localDate.toLocalEpochDays()))
    }

    override suspend fun deletePlan(id: Int) {
        dao.deletePlan(id)
    }

    override suspend fun addItem(planItem: PlanItem) {
        dao.insertPlanItem(planItem.toEntity())
    }

    override suspend fun removeItem(id: Long) {
        dao.deleteItem(id)
    }

    override suspend fun removeItemById(exerciseId: Int) {
        dao.deleteItemByExercise(exerciseId)
    }
}
