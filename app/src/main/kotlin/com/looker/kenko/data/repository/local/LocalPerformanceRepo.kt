package com.looker.kenko.data.repository.local

import com.looker.kenko.data.local.dao.SessionDao
import com.looker.kenko.data.local.dao.SetsDao
import com.looker.kenko.data.local.model.rating
import com.looker.kenko.data.model.Rating
import com.looker.kenko.data.model.plus
import com.looker.kenko.data.repository.PerformanceRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class LocalPerformanceRepo(
    private val sessionDao: SessionDao,
    private val setsDao: SetsDao,
) : PerformanceRepo {
    override fun exerciseBySessions(exerciseId: Int): Flow<Map<LocalDate, Rating>> =
        setsDao.setsByExerciseId(exerciseId).map {
            val ratingMap = hashMapOf<LocalDate, Rating>()
            for (set in it) {
                val setPerformedOn = sessionDao.getDatePerformedOn(set.sessionId)
                ratingMap.merge(setPerformedOn, set.rating, Rating::plus)
            }
            ratingMap
        }
}
