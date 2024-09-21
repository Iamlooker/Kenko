package com.looker.kenko.data.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Immutable
data class Plan(
    val name: String,
    val exercisesPerDay: Map<DayOfWeek, List<Exercise>>,
    val isActive: Boolean,
    val id: Int? = null,
)

val localDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

val week = DatePeriod(days = 7)
