package com.looker.kenko.data.repository

import com.looker.kenko.data.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepo {

    val stream: Flow<List<Exercise>>

    val numberOfExercise: Flow<Int>

    suspend fun get(id: Int): Exercise?

    suspend fun upsert(exercise: Exercise)

    suspend fun remove(id: Int)

    suspend fun isExerciseAvailable(name: String): Boolean

}
