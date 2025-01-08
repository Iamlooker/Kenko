package com.looker.kenko.data.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
data class Session(
    val date: LocalDate,
    val sets: List<Set>,
    val id: Int? = null,
)

fun Session(sets: List<Set>) = Session(date = localDate, sets = sets)

val Session.currentRating: Double
    get() = sets.sumOf { it.rating }
