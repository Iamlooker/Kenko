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

    override val stream: Flow<List<Session>>
        get() = dao.stream().map { it.map(SessionEntity::toExternal) }

    override suspend fun updateSet(sets: List<Set>) {
        dao.updateSets(date = localDate, sets = sets.map(Set::toEntity))
    }

    override suspend fun addSet(date: LocalDate, set: Set) {
        if (!date.isToday) error("Editing on old dates is not supported!")
        val currentSession = get(date) ?: error("For localDate it should not be null")
        val updatedSets = currentSession.sets.updateAsMutable { add(set) }
        dao.upsert(currentSession.copy(sets = updatedSets).toEntity())
    }

    override suspend fun removeSet(date: LocalDate, set: Set) {
        if (!date.isToday) error("Editing on old dates is not supported!")
        val currentSession = get(date) ?: error("For localDate it should not be null")
        val updatedSets = currentSession.sets.updateAsMutable { remove(set) }
        dao.upsert(currentSession.copy(sets = updatedSets).toEntity())
    }

    override suspend fun createEmpty() {
        val currentPlanId = requireNotNull(planDao.currentPlan()?.id) { "No plan active" }
        return dao.upsert(SessionEntity(localDate, emptyList(), currentPlanId))
    }

    override suspend fun get(date: LocalDate): Session? {
        val todaySession = dao.getSession(date)
        if (todaySession != null || !date.isToday) {
            return todaySession?.toExternal()
        }
        if (!date.isToday) {
            return dao.getSession(date)?.toExternal()
        }
        createEmpty()
        return dao.getSession(date)?.toExternal()
    }

    override fun getStream(date: LocalDate): Flow<Session?> {
        return dao.session(date).map(SessionEntity::toExternal)
    }

}