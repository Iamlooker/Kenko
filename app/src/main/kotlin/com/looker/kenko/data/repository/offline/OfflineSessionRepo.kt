package com.looker.kenko.data.repository.offline

import com.looker.kenko.data.local.dao.SessionDao
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.utils.isToday
import com.looker.kenko.utils.updateAsMutable
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class OfflineSessionRepo @Inject constructor(
    private val dao: SessionDao,
) : SessionRepo {

    override val stream: Flow<List<Session>>
        get() = dao.stream()

    override suspend fun updateSet(sets: List<Set>) {
        dao.updateSets(date = localDate, sets = sets)
    }

    override suspend fun addSet(date: LocalDate, set: Set) {
        if (!date.isToday) error("Editing on old dates is not supported!")
        val currentSession = get(date) ?: error("For localDate it should not be null")
        val updatedSets = currentSession.sets.updateAsMutable { add(set) }
        dao.upsert(currentSession.copy(sets = updatedSets))
    }

    override suspend fun createEmpty() {
        return dao.upsert(Session.create(emptyList()))
    }

    override suspend fun get(date: LocalDate): Session? {
        val todaySession = dao.getSession(date)
        if (todaySession != null || !date.isToday) {
            return todaySession
        }
        if (!date.isToday) {
            return dao.getSession(date)
        }
        createEmpty()
        return dao.getSession(date)
    }

    override fun getStream(date: LocalDate): Flow<Session?> {
        return dao.session(date)
    }

}