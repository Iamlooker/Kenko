package com.looker.kenko.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATASTORE_FILE_NAME = "settings"

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Provides
    @Singleton
    fun provideDatastore(
        @ApplicationContext context: Context,
    ) = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile(DATASTORE_FILE_NAME) }
    )

}