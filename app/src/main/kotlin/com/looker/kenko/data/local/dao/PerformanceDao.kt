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
        val query = buildString(1024) {
            append("SELECT sessions.date, ")
            // Sum of all ratings
            append("SUM(")

            // Ratings = reps * weight * set_type_modifier * rir_modifier
            append("sets.reps * ")
            append("sets.weight * ")
            append("set_type.modifier * ")

            // RIR modifier
            append("CASE WHEN sets.rir <= 0 ")
            append("THEN 1.20 WHEN sets.rir = 1 ")
            append("THEN 1.12 WHEN sets.rir = 2 ")
            append("THEN 1.04 WHEN sets.rir = 3 ")
            append("THEN 0.96 WHEN sets.rir = 4 ")
            append("THEN 0.88 ELSE 0.80 END")

            append(") AS rating FROM sets ")
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
                append("sessions.planId = ? ")
                selection.add(planId)
            }
            append("GROUP BY sessions.date ")
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
