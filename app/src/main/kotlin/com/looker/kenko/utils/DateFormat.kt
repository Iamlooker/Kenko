package com.looker.kenko.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration.Companion.days
import kotlinx.datetime.LocalDate

@JvmInline
value class DateFormat(private val value: String) {

    fun format(
        localDate: LocalDate,
        locale: Locale = Locale.getDefault(Locale.Category.FORMAT),
    ): String {
        val date = Date(localDate.toEpochDays().days.inWholeMilliseconds)
        val javaFormat = SimpleDateFormat(value, locale)
        return javaFormat.format(date)
    }

    companion object {
        val SessionLabel = DateFormat("dd-MMM")
        val BackupName = DateFormat("dd_MM")
    }
}

fun formatDate(
    date: LocalDate,
    dateTimeFormat: DateFormat = DateFormat.SessionLabel,
    locale: Locale = Locale.getDefault(Locale.Category.FORMAT),
): String = dateTimeFormat.format(localDate = date, locale = locale)
