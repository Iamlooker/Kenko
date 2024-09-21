package com.looker.kenko.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Plan
import kotlinx.datetime.DayOfWeek

@Entity(tableName = "plans")
data class PlanEntity(
    val name: String,
    val exercisesPerDay: Map<DayOfWeek, List<ExerciseEntity>>,
    val isActive: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

fun PlanEntity.toExternal(): Plan = Plan(
    id = id,
    name = name,
    exercisesPerDay = exercisesPerDay.mapValues { it.value.map(ExerciseEntity::toExternal) },
    isActive = isActive,
)

fun Plan.toEntity(): PlanEntity = PlanEntity(
    id = id ?: 0,
    name = name,
    exercisesPerDay = exercisesPerDay.mapValues { it.value.map(Exercise::toEntity) },
    isActive = isActive,
)
