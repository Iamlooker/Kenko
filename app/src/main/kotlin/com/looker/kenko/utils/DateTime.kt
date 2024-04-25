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

fun formatDate(
    date: LocalDate,
    dateTimeFormat: DateTimeFormat = DateTimeFormat.Short,
    locale: Locale = Locale.getDefault(Locale.Category.FORMAT),
): String {
    val format = SimpleDateFormat(dateTimeFormat.format, locale)
    return format.format(
        Date(date.toEpochDays().days.inWholeMilliseconds)
    )
}

val LocalDate.isToday: Boolean
    get() = daysUntil(Clock.System.todayIn(TimeZone.currentSystemDefault())) == 0

enum class DateTimeFormat(val format: String) {
    Short("dd-MMM"), Long("EEEE, dd-MMM-yyyy"),
    Day("EEEE")
}