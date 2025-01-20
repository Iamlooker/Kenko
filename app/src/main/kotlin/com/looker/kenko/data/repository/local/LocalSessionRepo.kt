package com.looker.kenko.data.repository.local

import com.looker.kenko.data.local.dao.ExerciseDao
import com.looker.kenko.data.local.dao.PlanHistoryDao
import com.looker.kenko.data.local.dao.SessionDao
import com.looker.kenko.data.local.dao.SetsDao
import com.looker.kenko.data.local.model.SessionDataEntity
import com.looker.kenko.data.local.model.SetEntity
import com.looker.kenko.data.local.model.toEntity
import com.looker.kenko.data.local.model.toExternal
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.utils.isToday
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import javax.inject.Inject

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

    override suspend fun addSet(set: Set) {
        if (!dao.sessionExistsOn(localDate)) {
            createEmpty(localDate)
        }
        val currentSessionId = requireNotNull(dao.getSessionId(localDate)) {
            "Session does not exist"
        }
        setsDao.insert(
            set.toEntity(
                currentSessionId,
                setsDao.getSetsCountBySessionId(currentSessionId) ?: 0,
            ),
        )
    }

    override suspend fun removeSet(setId: Int) {
        if (!dao.sessionExistsOn(localDate)) {
            error("Session does not exist so set cannot be removed")
        }
        setsDao.delete(setId)
    }

    override suspend fun createEmpty(date: LocalDate) {
        if (!date.isToday) error("Editing on old dates is not supported!")
        val currentPlanId = requireNotNull(historyDao.getCurrentId()) { "No plan active" }
        dao.insert(SessionDataEntity(localDate, currentPlanId))
    }

    override fun getStream(date: LocalDate): Flow<Session?> {
        return dao
            .session(date)
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
