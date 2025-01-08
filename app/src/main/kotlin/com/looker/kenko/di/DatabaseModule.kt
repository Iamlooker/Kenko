package com.looker.kenko.di

import android.content.Context
import com.looker.kenko.data.local.KenkoDatabase
import com.looker.kenko.data.local.dao.ExerciseDao
import com.looker.kenko.data.local.dao.PlanDao
import com.looker.kenko.data.local.dao.PlanHistoryDao
import com.looker.kenko.data.local.dao.SessionDao
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
    ): ExerciseDao = database.exerciseDao

    @Provides
    @Singleton
    fun provideSessionDao(
        database: KenkoDatabase,
    ): SessionDao = database.sessionDao

    @Provides
    @Singleton
    fun providePlanDao(
        database: KenkoDatabase,
    ): PlanDao = database.planDao

    @Provides
    @Singleton
    fun providePlanHistoryDao(
        database: KenkoDatabase,
    ): PlanHistoryDao = database.historyDao
}
