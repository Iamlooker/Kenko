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
