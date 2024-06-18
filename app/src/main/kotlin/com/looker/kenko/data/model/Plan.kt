package com.looker.kenko.data.model

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Stable
@Entity(tableName = "plan_table")
data class Plan(
    val name: String,
    val exercisesPerDay: Map<DayOfWeek, List<Exercise>>,
    val isActive: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
)

val localDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

val week = DatePeriod(days = 7)
