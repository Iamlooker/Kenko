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

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class RepsInReserve(val value: Int) {

    val modifier: Float
        get() = when {
            value <= 0 -> 1.20f
            value == 1 -> 1.12f
            value == 2 -> 1.04f
            value == 3 -> 0.96f
            value == 4 -> 0.88f
            else -> 0.80f
        }

    companion object {
        fun fromRPE(rpe: Int) = RepsInReserve(10 - rpe)
    }
}
