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

package com.looker.kenko.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.looker.kenko.data.local.dao.ExerciseDao
import com.looker.kenko.data.local.dao.PerformanceDao
import com.looker.kenko.data.local.dao.PlanDao
import com.looker.kenko.data.local.dao.PlanHistoryDao
import com.looker.kenko.data.local.dao.SessionDao
import com.looker.kenko.data.local.dao.SetsDao
import com.looker.kenko.data.local.model.ExerciseEntity
import com.looker.kenko.data.local.model.PlanDayEntity
import com.looker.kenko.data.local.model.PlanEntity
import com.looker.kenko.data.local.model.PlanHistoryEntity
import com.looker.kenko.data.local.model.SessionDataEntity
import com.looker.kenko.data.local.model.SetEntity
import com.looker.kenko.data.local.model.SetTypeEntity

@Database(
    version = 3,
    entities = [
        SessionDataEntity::class,
        ExerciseEntity::class,
        PlanEntity::class,
        PlanHistoryEntity::class,
        PlanDayEntity::class,
        SetEntity::class,
        SetTypeEntity::class,
    ],
)
abstract class KenkoDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun planDao(): PlanDao
    abstract fun setsDao(): SetsDao
    abstract fun historyDao(): PlanHistoryDao
    abstract fun performanceDao(): PerformanceDao
}

fun kenkoDatabase(context: Context) = Room
    .databaseBuilder(
        context = context,
        klass = KenkoDatabase::class.java,
        name = "kenko_database",
    )
    .createFromAsset("kenko.db")
    .addMigrations(
        MIGRATION_1_2,
        MIGRATION_2_3,
    )
    .build()
