package com.looker.kenko.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Rating
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.SetType

@Entity(
    "sets",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = SessionDataEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("sessionId", "exerciseId"),
    ],
)
data class SetEntity(
    @ColumnInfo("reps")
    val repsOrDuration: Int,
    val weight: Float,
    val type: SetType,
    val order: Int,
    val sessionId: Int,
    val exerciseId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)

val SetEntity.rating: Rating
    get() = Rating(repsOrDuration * weight * type.ratingModifier)

fun SetEntity.toExternal(exercise: Exercise): Set = Set(
    repsOrDuration = repsOrDuration,
    weight = weight,
    type = type,
    exercise = exercise,
    id = id,
)

fun Set.toEntity(sessionId: Int, order: Int): SetEntity = SetEntity(
    id = id ?: 0,
    repsOrDuration = repsOrDuration,
    weight = weight,
    type = type,
    order = order,
    sessionId = sessionId,
    exerciseId = requireNotNull(exercise.id),
)
