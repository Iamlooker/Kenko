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
    val isActive: Boolean,
    val stat: PlanStat = PlanStat(0, 0),
    val id: Int? = null,
)

@Immutable
data class PlanItem(
    val dayOfWeek: DayOfWeek,
    val exercise: Exercise,
    val planId: Int,
    val id: Long? = null,
)

val localDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

val week = DatePeriod(days = 7)
