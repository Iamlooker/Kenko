/*
 * Copyright (C) 2025 LooKeR & Contributors
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.looker.kenko.data.repository.local

import com.looker.kenko.data.local.dao.ExerciseDao
import com.looker.kenko.data.local.model.ExerciseEntity
import com.looker.kenko.data.local.model.toEntity
import com.looker.kenko.data.local.model.toExternal
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.repository.ExerciseRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalExerciseRepo @Inject constructor(
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
