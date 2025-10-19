/*
 * Copyright (C) 2025. LooKeR & Contributors
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

package com.looker.kenko.data.repository

import com.looker.kenko.data.model.settings.ColorPalettes
import com.looker.kenko.data.model.settings.Settings
import com.looker.kenko.data.model.settings.Theme
import kotlin.time.Instant
import kotlinx.coroutines.flow.Flow

interface SettingsRepo {

    val stream: Flow<Settings>

    fun <T> get(block: Settings.() -> T): Flow<T>

    suspend fun setOnboardingDone()

    suspend fun setColorPalette(colorPalette: ColorPalettes)

    suspend fun setTheme(theme: Theme)

    suspend fun setLastSetTime(instant: Instant?)

}
