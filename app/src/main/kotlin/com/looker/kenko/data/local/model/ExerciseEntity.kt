package com.looker.kenko.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.MuscleGroups
import kotlinx.serialization.Serializable

@Serializable
@Entity("exercises")
data class ExerciseEntity(
    val name: String,
    val target: MuscleGroups,
    val reference: String? = null,
    val isIsometric: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
)

fun ExerciseEntity.toExternal(): Exercise = Exercise(
    id = id,
    name = name,
    target = target,
    reference = reference,
    isIsometric = isIsometric
)

fun Exercise.toEntity(): ExerciseEntity = ExerciseEntity(
    id = id,
    name = name,
    target = target,
    reference = reference,
    isIsometric = isIsometric
)
