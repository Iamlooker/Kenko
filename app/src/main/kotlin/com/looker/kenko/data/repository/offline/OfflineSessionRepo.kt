package com.looker.kenko.data.repository.offline

import com.looker.kenko.data.local.dao.PlanDao
import com.looker.kenko.data.local.dao.SessionDao
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.model.localDayOfWeek
import com.looker.kenko.data.repository.SessionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class OfflineSessionRepo @Inject constructor(
    private val dao: SessionDao,
    private val planDao: PlanDao,
) : SessionRepo {

    override val stream: Flow<List<Session>>
        get() = dao.stream()

    override val exercisesToday: Flow<List<Exercise>?>
        get() = planDao
            .currentPlanStream(true)
            .map { plan: Plan? -> plan?.exercisesPerDay?.get(localDayOfWeek) }

    override suspend fun updateSet(sets: List<Set>) {
        dao.updateSets(date = localDate, sets = sets)
    }

    override suspend fun createEmpty() {
        return dao.upsert(Session.create(emptyList()))
    }

    override suspend fun get(date: LocalDate): Session? {
        return dao.getSession(date)
    }

    override fun getStream(date: LocalDate): Flow<Session?> {
        return dao.session(date)
    }

}