/*
 * Copyright (C) 2025. LooKeR & Contributors
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

package com.looker.kenko.data.progression

data class SetPerformance(
    val weight: Float,
    val reps: Int,
    val rir: Int,
)

enum class ProgressTarget { Weight, Reps, Sets }

enum class DeloadScope { Exercise, MuscleGroup, Plan }

sealed class Decision {
    data class Progress(val target: ProgressTarget, val amount: Float) : Decision()
    data object Continue : Decision()
    data class Deload(val scope: DeloadScope) : Decision()
}

class SessionProgression {
    fun suggestNext(lastSession: List<SetPerformance>): Decision {
        if (lastSession.isEmpty()) return Decision.Continue
        val avgRir = lastSession.map { it.rir }.average()
        return when {
            avgRir >= 2.5 -> Decision.Progress(ProgressTarget.Weight, 2.5f)
            avgRir < 1.0 -> Decision.Deload(DeloadScope.Exercise)
            else -> Decision.Continue
        }
    }
}
