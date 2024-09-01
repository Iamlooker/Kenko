package com.looker.kenko.data.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class Set(
    val repsOrDuration: Int,
    val weight: Double,
    val type: SetType,
    val exercise: Exercise,
)

val Set.rating: Double
    get() = repsOrDuration * weight * type.ratingModifier

enum class SetType(val ratingModifier: Double) {
    Standard(STANDARD_SET_RATING_MODIFIER),
    Drop(DROP_SET_RATING_MODIFIER),
    RestPause(REST_PAUSE_SET_RATING_MODIFIER),
}

private const val STANDARD_SET_RATING_MODIFIER: Double = 1.0
private const val DROP_SET_RATING_MODIFIER: Double = 1.35
private const val REST_PAUSE_SET_RATING_MODIFIER: Double = 1.2

val Set.Companion.Samples: List<Set>
    get() = listOf(
    )
