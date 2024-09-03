package com.looker.kenko.data.repository.offline

import com.looker.kenko.data.local.dao.ExerciseDao
import com.looker.kenko.data.local.model.ExerciseEntity
import com.looker.kenko.data.local.model.toEntity
import com.looker.kenko.data.local.model.toExternal
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.repository.ExerciseRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineExerciseRepo @Inject constructor(
    private val dao: ExerciseDao,
) : ExerciseRepo {

    override val stream: Flow<List<Exercise>> =
        dao.stream().map { it.map(ExerciseEntity::toExternal) }

    override suspend fun get(name: String): Exercise? {
        return dao.get(name)?.toExternal()
    }

    override suspend fun upsert(exercise: Exercise) {
        dao.upsert(exercise.toEntity())
    }

    override suspend fun remove(name: String) {
        dao.delete(name)
    }

    override suspend fun isExerciseAvailable(name: String): Boolean {
        return get(name) != null
    }

}