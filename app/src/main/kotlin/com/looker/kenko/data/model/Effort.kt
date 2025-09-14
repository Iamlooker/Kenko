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

package com.looker.kenko.data.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class RIR(val value: Int) {
    companion object {
        fun fromRPE(rpe: RPE) = RIR(10 - rpe.value)
        fun fromRPE(rpe: Int) = RIR(10 - rpe)
    }
}

@Serializable
@JvmInline
value class RPE(val value: Int) {
    companion object {
        fun fromRIR(rir: RIR) = RPE((10 - rir.value).coerceIn(1, 10))
        fun fromRIR(rir: Int) = RPE((10 - rir).coerceIn(1, 10))
    }
}