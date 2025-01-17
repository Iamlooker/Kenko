package com.looker.kenko.data.repository

import com.looker.kenko.data.model.Rating
import kotlinx.coroutines.flow.Flow

interface PerformanceRepo {

    fun exerciseBySessions(exerciseId: Int): Flow<Map<String, Rating>>

}
