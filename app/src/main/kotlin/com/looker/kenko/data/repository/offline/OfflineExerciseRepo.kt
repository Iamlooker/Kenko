package com.looker.kenko.data.repository.offline

import com.looker.kenko.data.local.dao.ExerciseDao
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.repository.ExerciseRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineExerciseRepo @Inject constructor (
    private val dao: ExerciseDao,
) : ExerciseRepo {

    override val stream: Flow<List<Exercise>>
        get() = dao.stream()

    override suspend fun get(name: String): Exercise? {
        return dao.get(name)
    }

    override suspend fun upsert(exercise: Exercise) {
        dao.upsert(exercise)
    }

    override suspend fun isExerciseAvailable(name: String): Boolean {
        return get(name) != null
    }

}