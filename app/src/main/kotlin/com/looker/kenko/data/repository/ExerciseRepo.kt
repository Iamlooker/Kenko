package com.looker.kenko.data.repository

import com.looker.kenko.data.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepo {

    val stream: Flow<List<Exercise>>

    suspend fun get(name: String): Exercise?

    suspend fun upsert(exercise: Exercise)

    suspend fun isExerciseAvailable(name: String): Boolean

}