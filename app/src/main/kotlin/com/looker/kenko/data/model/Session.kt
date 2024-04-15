package com.looker.kenko.data.model

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity
@Stable
data class Session(
    @PrimaryKey
    val date: LocalDate,
    val sets: List<Set>,
) {
    companion object {
        fun create(sets: List<Set>): Session {
            return Session(
                date = localDate,
                sets = sets,
            )
        }

        val SAMPLE = Session(
            LocalDate(2024, 4, 15),
            sets = emptyList()
        )
    }
}
