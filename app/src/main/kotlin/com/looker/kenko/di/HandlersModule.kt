package com.looker.kenko.di

import android.content.Context
import androidx.compose.ui.platform.UriHandler
import com.looker.kenko.data.KenkoUriHandler
import com.looker.kenko.data.StringHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object HandlersModule {

    @Provides
    @ViewModelScoped
    fun provideContext(
        @ApplicationContext context: Context
    ): Context = context

    @Provides
    @ViewModelScoped
    fun provideUriHandler(
        @ApplicationContext context: Context
    ): UriHandler = KenkoUriHandler(context)

    @Provides
    @ViewModelScoped
    fun provideStringHandler(
        @ApplicationContext context: Context
    ): StringHandler = StringHandler(context)
}
