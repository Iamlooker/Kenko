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

import com.looker.kenko.data.local.datastore.DatastoreSettingsRepo
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.data.repository.PerformanceRepo
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.data.repository.SettingsRepo
import com.looker.kenko.data.repository.local.LocalExerciseRepo
import com.looker.kenko.data.repository.local.LocalPerformanceRepo
import com.looker.kenko.data.repository.local.LocalPlanRepo
import com.looker.kenko.data.repository.local.LocalSessionRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSessionRepo(
        repo: LocalSessionRepo,
    ): SessionRepo

    @Binds
    abstract fun bindPlanRepo(
        repo: LocalPlanRepo,
    ): PlanRepo

    @Binds
    abstract fun bindExerciseRepo(
        repo: LocalExerciseRepo,
    ): ExerciseRepo

    @Binds
    abstract fun bindPerformanceRepo(
        repo: LocalPerformanceRepo,
    ): PerformanceRepo

    @Binds
    abstract fun bindSettingsRepo(
        repo: DatastoreSettingsRepo,
    ): SettingsRepo
}
