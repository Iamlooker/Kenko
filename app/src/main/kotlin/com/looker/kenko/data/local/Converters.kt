package com.looker.kenko.data.local

import androidx.room.TypeConverter
import com.looker.kenko.data.local.model.ExerciseEntity
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.serializers.DayOfWeekSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.json.Json

private val json = Json
private val exerciseMapSerializer =
    MapSerializer(DayOfWeekSerializer, ListSerializer(ExerciseEntity.serializer()))

class Converters {

    @TypeConverter
    fun fromLocalDate(value: LocalDate): Int {
        return value.toEpochDays()
    }

    @TypeConverter
    fun toLocalDate(value: Int): LocalDate {
        return LocalDate.fromEpochDays(value)
    }

    @TypeConverter
    fun fromExerciseMap(value: Map<DayOfWeek, List<ExerciseEntity>>): String {
        return json.encodeToString(exerciseMapSerializer, value)
    }

    @TypeConverter
    fun toExerciseMap(value: String): Map<DayOfWeek, List<ExerciseEntity>> {
        return json.decodeFromString(exerciseMapSerializer, value)
    }

}