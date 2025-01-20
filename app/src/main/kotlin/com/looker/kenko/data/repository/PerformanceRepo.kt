package com.looker.kenko.data.repository

import com.looker.kenko.data.model.Rating
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface PerformanceRepo {

    fun exerciseBySessions(exerciseId: Int): Flow<Map<LocalDate, Rating>>

}
