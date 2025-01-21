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

package com.looker.kenko.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.days

@JvmInline
value class EpochDays(val value: Int)

operator fun EpochDays.plus(other: EpochDays) = EpochDays(value + other.value)

fun LocalDate.toLocalEpochDays() = EpochDays(toEpochDays())

fun formatDate(
    date: LocalDate,
    dateTimeFormat: DateTimeFormat = DateTimeFormat.Short,
    locale: Locale = Locale.getDefault(Locale.Category.FORMAT),
): String {
    val format = SimpleDateFormat(dateTimeFormat.format, locale)
    return format.format(
        Date(date.toEpochDays().days.inWholeMilliseconds),
    )
}

fun formatDate(
    epochDays: Int,
    dateTimeFormat: DateTimeFormat = DateTimeFormat.Short,
    locale: Locale = Locale.getDefault(Locale.Category.FORMAT),
): String {
    val format = SimpleDateFormat(dateTimeFormat.format, locale)
    return format.format(
        Date(epochDays.days.inWholeMilliseconds),
    )
}

val LocalDate.isToday: Boolean
    get() = daysUntil(Clock.System.todayIn(TimeZone.currentSystemDefault())) == 0

enum class DateTimeFormat(val format: String) {
    Short("dd-MMM"), Long("EEEE, dd-MMM-yyyy"),
    Day("EEEE")
}
