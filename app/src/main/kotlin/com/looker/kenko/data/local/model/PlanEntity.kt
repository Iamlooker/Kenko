package com.looker.kenko.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.model.PlanItem
import com.looker.kenko.data.model.PlanStat
import kotlinx.datetime.DayOfWeek

@Entity(tableName = "plans")
data class PlanEntity(
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)

@Entity(
    "plan_day",
    foreignKeys = [
        ForeignKey(
            entity = PlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["planId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("planId", "exerciseId"),
    ],
)
data class PlanDayEntity(
    val planId: Int,
    val exerciseId: Int,
    val dayOfWeek: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
)

fun PlanEntity.toExternal(isActive: Boolean, stat: PlanStat) = Plan(
    id = id,
    name = name,
    stat = stat,
    isActive = isActive,
)

fun Plan.toEntity(): PlanEntity = PlanEntity(
    id = id ?: 0,
    name = name,
)

fun PlanItem.toEntity() = PlanDayEntity(
    id = id ?: 0,
    planId = planId,
    exerciseId = requireNotNull(exercise.id) { "Exercise id cannot be null" },
    dayOfWeek = dayOfWeek.value,
)

inline fun PlanDayEntity.toExternal(block: (exerciseId: Int) -> Exercise?) = PlanItem(
    planId = planId,
    dayOfWeek = DayOfWeek.of(dayOfWeek),
    exercise = block(exerciseId) ?: DefaultExercise,
    id = id,
)

val DefaultExercise = Exercise(
    name = "Exercise Deleted",
    target = MuscleGroups.Core,
)
