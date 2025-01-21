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
import com.looker.kenko.data.local.dao.PlanHistoryDao
import com.looker.kenko.data.local.dao.SessionDao
import com.looker.kenko.data.local.dao.SetsDao
import com.looker.kenko.data.local.model.SetEntity
import com.looker.kenko.data.local.model.rating
import com.looker.kenko.data.local.model.toExternal
import com.looker.kenko.data.repository.Performance
import com.looker.kenko.data.repository.PerformanceRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2

class LocalPerformanceRepo @Inject constructor(
    private val sessionDao: SessionDao,
    private val exerciseDao: ExerciseDao,
    private val setsDao: SetsDao,
    private val planHistoryDao: PlanHistoryDao,
) : PerformanceRepo {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun performance(
        exerciseId: Int?,
        planId: Int?,
    ): Flow<Performance?> {
        if (exerciseId == null && planId == null) {
            return planHistoryDao.currentIdFlow().map { currentId ->
                if (currentId == null) return@map null
                performanceOf(setsDao.getSetsByExerciseIdPerPlan(planId = currentId))
            }
        }
        return setsDao.setsByExerciseIdPerPlan(planId, exerciseId).map { sets ->
            performanceOf(sets)
        }
    }

    override suspend fun getPerformance(
        exerciseId: Int?,
        planId: Int?,
    ): Performance? {
        val adjustedPlanId = if (exerciseId == null && planId == null) {
            planHistoryDao.getCurrentId() ?: return null
        } else {
            planId
        }
        return performanceOf(setsDao.getSetsByExerciseIdPerPlan(adjustedPlanId, exerciseId))
    }

    private suspend fun performanceOf(sets: List<SetEntity>): Performance {
        val bySessionId = sets.groupBy { it.sessionId }
        val days = IntArray(bySessionId.size)
        val ratings = DoubleArray(bySessionId.size)
        var index = 0
        bySessionId.forEach { (sessionId, sets) ->
            days[index] = sessionDao.getDatePerformedOn(sessionId).value
            ratings[index] = sets.rating.value
            index++
        }
        val (first, last) = run {
            val firstSet = sets.first()
            val lastSet = sets.last()
            val firstExercise = exerciseDao.get(firstSet.exerciseId)!!.toExternal()
            val lastExercise = exerciseDao.get(lastSet.exerciseId)!!.toExternal()
            firstSet.toExternal(firstExercise) to lastSet.toExternal(lastExercise)
        }
        return Performance(days, ratings, first, last)
    }
}
