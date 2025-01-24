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

package com.looker.kenko.data.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Set(
    val repsOrDuration: Int,
    val weight: Float,
    val type: SetType,
    val exercise: Exercise,
    val id: Int? = null,
)

val Set.rating: Rating
    get() = Rating(repsOrDuration * weight * type.ratingModifier)

enum class SetType(val ratingModifier: Float) {
    Standard(STANDARD_SET_RATING_MODIFIER),
    Drop(DROP_SET_RATING_MODIFIER),
    RestPause(REST_PAUSE_SET_RATING_MODIFIER),
}

private const val STANDARD_SET_RATING_MODIFIER: Float = 1.0F
private const val DROP_SET_RATING_MODIFIER: Float = 1.35F
private const val REST_PAUSE_SET_RATING_MODIFIER: Float = 1.2F
