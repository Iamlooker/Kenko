package com.looker.kenko.data.repository.offline

import com.looker.kenko.data.local.dao.PlanDao
import com.looker.kenko.data.local.dao.SessionDao
import com.looker.kenko.data.local.model.SessionEntity
import com.looker.kenko.data.local.model.toEntity
import com.looker.kenko.data.local.model.toExternal
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.utils.isToday
import com.looker.kenko.utils.updateAsMutable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class OfflineSessionRepo @Inject constructor(
    private val dao: SessionDao,
    private val planDao: PlanDao,
) : SessionRepo {

    override val stream: Flow<List<Session>> =
        dao.stream().map { it.map(SessionEntity::toExternal) }

    override suspend fun updateSet(sets: List<Set>) {
        val session = get(localDate)!!
        val currentPlan = requireNotNull(planDao.currentPlanId()) { "No plan active" }
        dao.upsert(session.copy(sets = sets).toEntity(currentPlan))
    }

    override suspend fun addSet(set: Set) {
        val currentSession = requireNotNull(get(localDate)) { "For localDate it should not be null" }
        val currentPlan = requireNotNull(planDao.currentPlanId()) { "No plan active" }
        val updatedSets = currentSession.sets.updateAsMutable { add(set) }
        dao.upsert(currentSession.copy(sets = updatedSets).toEntity(currentPlan))
    }

    override suspend fun removeSet(set: Set) {
        val currentSession = requireNotNull(get(localDate)) { "For localDate it should not be null" }
        val currentPlan = requireNotNull(planDao.currentPlanId()) { "No plan active" }
        val updatedSets = currentSession.sets.updateAsMutable { remove(set) }
        dao.upsert(currentSession.copy(sets = updatedSets).toEntity(currentPlan))
    }

    override suspend fun createEmpty(date: LocalDate) {
        if (!date.isToday) error("Editing on old dates is not supported!")
        val currentPlanId = requireNotNull(planDao.currentPlanId()) { "No plan active" }
        dao.upsert(SessionEntity(localDate, emptyList(), currentPlanId))
    }

    override suspend fun get(date: LocalDate): Session? {
        if (!dao.sessionExistsOn(date)) {
            if (!date.isToday) return null
            createEmpty(date)
        }
        return dao.getSession(date)?.toExternal()
    }

    override fun getStream(date: LocalDate): Flow<Session?> {
        return dao.session(date).map { it?.toExternal() }
    }

}