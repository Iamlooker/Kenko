/*
 * Copyright (C) 2025. LooKeR & Contributors
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
import com.looker.kenko.data.local.model.SessionDataEntity
import com.looker.kenko.data.local.model.SetEntity
import com.looker.kenko.data.local.model.SetType
import com.looker.kenko.data.local.model.toEntity
import com.looker.kenko.data.local.model.toExternal
import com.looker.kenko.data.model.RIR
import com.looker.kenko.data.model.RPE
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.utils.toLocalEpochDays
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class LocalSessionRepo @Inject constructor(
    private val dao: SessionDao,
    private val setsDao: SetsDao,
    private val historyDao: PlanHistoryDao,
    private val exerciseDao: ExerciseDao,
) : SessionRepo {

    override val stream: Flow<List<Session>> =
        dao.stream().map {
            it.map { session ->
                session.toExternal(session.sets.toExternal())
            }
        }
    override val setsCount: Flow<Int> =
        setsDao.totalSetCount()

    override suspend fun addSet(sessionId: Int, set: Set) {
        setsDao.insert(
            set.toEntity(
                sessionId,
                setsDao.getSetsCountBySessionId(sessionId) ?: 0,
            ),
        )
    }

    override suspend fun addSet(
        sessionId: Int,
        exerciseId: Int,
        weight: Float,
        reps: Int,
        setType: SetType,
        rir: RIR,
        rpe: RPE,
    ) {
        setsDao.insert(
            SetEntity(
                repsOrDuration = reps,
                weight = weight,
                exerciseId = exerciseId,
                sessionId = sessionId,
                type = setType,
                order = setsDao.getSetsCountBySessionId(sessionId) ?: 0,
                rpe = rpe.value,
                rir = rir.value,
            ),
        )
    }

    override suspend fun removeSet(setId: Int) {
        if (!dao.sessionExistsOn(localDate.toLocalEpochDays())) {
            error("Session does not exist so set cannot be removed")
        }
        setsDao.delete(setId)
    }

    override suspend fun getSessionIdOrCreate(date: LocalDate): Int {
        val currentPlanId = requireNotNull(historyDao.getCurrentId()) { "No plan active" }
        val existingId = dao.getSessionId(date.toLocalEpochDays())
        if (existingId != null) {
            return existingId
        }
        return dao.insert(SessionDataEntity(date.toLocalEpochDays(), currentPlanId)).toInt()
    }

    override fun streamByDate(date: LocalDate): Flow<Session?> {
        return dao
            .session(date.toLocalEpochDays())
            .map { session ->
                if (session == null) return@map null
                session.toExternal(session.sets.toExternal())
            }
    }

    override suspend fun getSets(sessionId: Int): List<Set> =
        setsDao.getSetsBySessionId(sessionId).toExternal()

    private suspend fun List<SetEntity>.toExternal(): List<Set> = mapNotNull {
        val exercise = exerciseDao.get(it.exerciseId) ?: return@mapNotNull null
        it.toExternal(exercise.toExternal())
    }
}
