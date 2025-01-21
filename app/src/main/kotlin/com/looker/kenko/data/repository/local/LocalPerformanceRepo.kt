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

package com.looker.kenko.data.repository.local

import com.looker.kenko.data.local.dao.SessionDao
import com.looker.kenko.data.local.dao.SetsDao
import com.looker.kenko.data.local.model.rating
import com.looker.kenko.data.model.Rating
import com.looker.kenko.data.model.plus
import com.looker.kenko.data.repository.PerformanceRepo
import com.looker.kenko.utils.EpochDays
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class LocalPerformanceRepo @Inject constructor(
    private val sessionDao: SessionDao,
    private val setsDao: SetsDao,
) : PerformanceRepo {
    override fun exerciseBySessions(exerciseId: Int): Flow<Pair<Array<EpochDays>, Array<Rating>>> =
        TODO("Not implemented")
}
