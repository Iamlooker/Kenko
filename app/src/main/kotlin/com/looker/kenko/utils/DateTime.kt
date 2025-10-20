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

import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

@JvmInline
value class EpochDays(val value: Int)

operator fun EpochDays.plus(other: EpochDays) = EpochDays(value + other.value)

fun LocalDate.toLocalEpochDays() = EpochDays(toEpochDays().toInt())

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

inline operator fun DayOfWeek.plus(days: Int): DayOfWeek {
    val amount = (days % 7)
    return DayOfWeek(((ordinal + (amount + 7)) % 7) + 1)
}

inline operator fun DayOfWeek.minus(days: Int): DayOfWeek = plus(-days)

fun Random.nextInstant(
    from: Instant = Instant.DISTANT_PAST,
    until: Instant = Instant.DISTANT_FUTURE,
): Instant = Instant.fromEpochMilliseconds(
    nextLong(
        from = from.toEpochMilliseconds(),
        until = until.toEpochMilliseconds(),
    ),
)

fun Random.nextLocalDateTime(
    from: Instant = Instant.DISTANT_PAST,
    until: Instant = Instant.DISTANT_FUTURE,
): LocalDateTime = nextInstant(from, until)
    .toLocalDateTime(TimeZone.currentSystemDefault())

val LocalDate.isToday: Boolean
    get() = daysUntil(Clock.System.todayIn(TimeZone.currentSystemDefault())) == 0

enum class DateTimeFormat(val format: String) {
    Short("dd-MMM"), Long("EEEE, dd-MMM-yyyy"),
    Day("EEEE")
}
