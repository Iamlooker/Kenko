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

package com.looker.kenko.data.local.dao

import androidx.room.Dao
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import com.looker.kenko.data.local.model.SetTypeEntity
import com.looker.kenko.data.repository.Performance

@Dao
interface PerformanceDao {

    @Upsert
    suspend fun upsertSetTypeLookup(type: List<SetTypeEntity>)

    @RawQuery
    suspend fun _rawQueryRatingWrappers(query: SimpleSQLiteQuery): List<RatingWrapper>?

    @Transaction
    suspend fun getPerformance(exerciseId: Int?, planId: Int?): Performance? {
        val selection = arrayListOf<Any?>()
        val query = buildString(512) {
            append("SELECT sessions.date, sets.reps * sets.weight * set_type.modifier AS rating FROM sets ")
            append("INNER JOIN set_type ON sets.type = set_type.type ")
            append("INNER JOIN sessions ON sets.sessionId = sessions.id ")
            if (exerciseId != null) {
                append("WHERE (sets.exerciseId = ?) ")
                selection.add(exerciseId)
            }
            if (planId != null) {
                if (exerciseId != null) {
                    append("AND ")
                } else {
                    append("WHERE ")
                }
                append("sessionId IN (SELECT id FROM sessions WHERE planId = ?) ")
                selection.add(planId)
            }
            append("ORDER BY sessions.date ASC")
        }
        val ratingWrapper = _rawQueryRatingWrappers(
            SimpleSQLiteQuery(
                query = query,
                bindArgs = selection.toTypedArray(),
            ),
        )
        return ratingWrapper?.toPerformance()
    }

}

class RatingWrapper(
    val date: Int,
    val rating: Float,
)

fun List<RatingWrapper>.toPerformance(): Performance {
    val days = IntArray(size)
    val ratings = FloatArray(size)
    for (i in indices) {
        val rating = get(i)
        days[i] = rating.date
        ratings[i] = rating.rating
    }
    return Performance(
        days = days,
        ratings = ratings,
    )
}
