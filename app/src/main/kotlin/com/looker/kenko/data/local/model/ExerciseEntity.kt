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

package com.looker.kenko.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.MuscleGroups
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("exercise")
@Entity("exercises")
data class ExerciseEntity(
    val name: String,
    val target: MuscleGroups,
    val reference: String? = null,
    val isIsometric: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
fun ExerciseEntity.toExternal(): Exercise = Exercise(
    id = id,
    name = name,
    target = target,
    reference = reference,
    isIsometric = isIsometric
)

fun Exercise.toEntity(): ExerciseEntity = ExerciseEntity(
    id = id ?: 0,
    name = name,
    target = target,
    reference = reference,
    isIsometric = isIsometric
)
