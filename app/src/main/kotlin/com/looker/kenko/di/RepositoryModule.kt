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
