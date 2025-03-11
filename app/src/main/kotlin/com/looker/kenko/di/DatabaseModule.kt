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

package com.looker.kenko.di

import android.content.Context
import com.looker.kenko.data.local.KenkoDatabase
import com.looker.kenko.data.local.dao.ExerciseDao
import com.looker.kenko.data.local.dao.PerformanceDao
import com.looker.kenko.data.local.dao.PlanDao
import com.looker.kenko.data.local.dao.PlanHistoryDao
import com.looker.kenko.data.local.dao.SessionDao
import com.looker.kenko.data.local.dao.SetsDao
import com.looker.kenko.data.local.kenkoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): KenkoDatabase = kenkoDatabase(context)

    @Provides
    @Singleton
    fun provideExerciseDao(
        database: KenkoDatabase,
    ): ExerciseDao = database.exerciseDao()

    @Provides
    @Singleton
    fun provideSessionDao(
        database: KenkoDatabase,
    ): SessionDao = database.sessionDao()

    @Provides
    @Singleton
    fun providePlanDao(
        database: KenkoDatabase,
    ): PlanDao = database.planDao()

    @Provides
    @Singleton
    fun provideSetsDao(
        database: KenkoDatabase,
    ): SetsDao = database.setsDao()

    @Provides
    @Singleton
    fun providePlanHistoryDao(
        database: KenkoDatabase,
    ): PlanHistoryDao = database.historyDao()

    @Provides
    @Singleton
    fun providePerformanceDao(
        database: KenkoDatabase,
    ): PerformanceDao = database.performanceDao()
}
