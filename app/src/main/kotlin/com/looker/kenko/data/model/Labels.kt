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

// TODO: Empahsis on these being a suggestion, "These labels are just suggestions,
//  even if you are advanced you can use a beginner plan
//  and change it to your liking and use it"
//  also clarify how you can recognize your own place in this system,
//  1-3 yrs is beginners, and so on
sealed interface Labels {
    enum class Difficulty : Labels {
        // More free weight exercises, and generally slower plans
        BEGINNER,
        INTERMEDIATE,
        ADVANCED,
        // Generally need personal additions
        ADAPTABLE,
    }

    enum class Focus : Labels {
        STRENGTH,
        HYPERTROPHY,
        POWER_BUILDING,
    }

    enum class Equipment : Labels {
        FULL_GYM,
        DUMBBELLS,
        BARBELLS,
        NONE,
    }

    enum class Time : Labels {
        QUICK,
        NORMAL,
    }
}
