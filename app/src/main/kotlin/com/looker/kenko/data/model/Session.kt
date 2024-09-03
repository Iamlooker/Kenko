package com.looker.kenko.data.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
data class Session(
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

val Session.currentRating: Double
    get() = sets.sumOf { it.rating }
