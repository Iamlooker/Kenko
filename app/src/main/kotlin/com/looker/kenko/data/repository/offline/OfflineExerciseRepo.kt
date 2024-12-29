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

    override val numberOfExercise: Flow<Int> = dao.number()

    override suspend fun get(id: Int): Exercise? =
        dao.get(id)?.toExternal()

    override suspend fun upsert(exercise: Exercise) {
        dao.upsert(exercise.toEntity())
    }

    override suspend fun remove(id: Int) {
        dao.delete(id)
    }

    override suspend fun isExerciseAvailable(name: String): Boolean =
        dao.exists(name)

}
