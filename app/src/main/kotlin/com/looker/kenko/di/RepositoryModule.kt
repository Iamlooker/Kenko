package com.looker.kenko.di

import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.data.repository.offline.OfflineExerciseRepo
import com.looker.kenko.data.repository.offline.OfflinePlanRepo
import com.looker.kenko.data.repository.offline.OfflineSessionRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSessionRepo(
        repo: OfflineSessionRepo,
    ): SessionRepo

    @Binds
    abstract fun bindPlanRepo(
        repo: OfflinePlanRepo,
    ): PlanRepo

    @Binds
    abstract fun bindExerciseRepo(
        repo: OfflineExerciseRepo,
    ): ExerciseRepo

}