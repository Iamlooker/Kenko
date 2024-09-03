package com.looker.kenko.data.local.model

import com.looker.kenko.data.model.SetType
import com.looker.kenko.data.model.Set
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("set")
data class SetEntity(
    val repsOrDuration: Int,
    val weight: Float,
    val type: SetType,
    val exercise: ExerciseEntity,
)

fun SetEntity.toExternal(): Set = Set(
    repsOrDuration = repsOrDuration,
    weight = weight,
    type = type,
    exercise = exercise.toExternal(),
)

fun Set.toEntity(): SetEntity = SetEntity(
    repsOrDuration = repsOrDuration,
    weight = weight,
    type = type,
    exercise = exercise.toEntity(),
)
