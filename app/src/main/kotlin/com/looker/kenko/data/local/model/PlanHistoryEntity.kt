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

package com.looker.kenko.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.looker.kenko.utils.EpochDays

@Entity(
    tableName = "plan_history",
    foreignKeys = [
        ForeignKey(
            entity = PlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["planId"],
            onDelete = ForeignKey.Companion.SET_NULL
        )
    ],
    indices = [
        Index("planId", "start", "end")
    ]
)
data class PlanHistoryEntity(
    val planId: Int?,
    val start: EpochDays,
    @ColumnInfo(defaultValue = "NULL")
    val end: EpochDays? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
