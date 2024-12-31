package com.looker.kenko.data.local

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class Converters {
    @TypeConverter
    fun fromLocalDate(value: LocalDate): Int {
        return value.toEpochDays()
    }

    @TypeConverter
    fun toLocalDate(value: Int): LocalDate {
        return LocalDate.fromEpochDays(value)
    }
}
