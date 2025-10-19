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

package com.looker.kenko.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.looker.kenko.BuildConfig
import com.looker.kenko.data.model.settings.ColorPalettes
import com.looker.kenko.data.model.settings.Settings
import com.looker.kenko.data.model.settings.Theme
import com.looker.kenko.data.repository.SettingsRepo
import javax.inject.Inject
import kotlin.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class DatastoreSettingsRepo @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepo {

    override val stream: Flow<Settings>
        get() = dataStore.data
            .catch { if (it is IOException) error("Error reading datastore") }
            .map(::mapSettings)

    override fun <T> get(block: Settings.() -> T): Flow<T> {
        return stream.map { it.block() }
    }

    override suspend fun setOnboardingDone() {
        if (!BuildConfig.DEBUG) {
            ONBOARDING_DONE.update(true)
        }
    }

    override suspend fun setColorPalette(colorPalette: ColorPalettes) {
        COLOR_PALETTE.update(colorPalette.name)
    }

    override suspend fun setTheme(theme: Theme) {
        THEME.update(theme.name)
    }

    override suspend fun setLastSetTime(instant: Instant?) {
        dataStore.edit { preference ->
            if (instant != null) {
                preference[LAST_SET_TIME_SECONDS] = instant.epochSeconds
            } else {
                preference.remove(LAST_SET_TIME_SECONDS)
            }
        }
    }

    private suspend inline fun <T> Preferences.Key<T>.update(value: T) {
        dataStore.edit { preference ->
            preference[this] = value
        }
    }

    private fun mapSettings(preferences: Preferences): Settings {
        val isOnboardingDone = preferences[ONBOARDING_DONE] ?: false
        val theme = preferences[THEME] ?: Theme.System.name
        val colorPalettes = preferences[COLOR_PALETTE] ?: ColorPalettes.Zestful.name
        val lastSetTime = preferences[LAST_SET_TIME_SECONDS]
        return Settings(
            isOnboardingDone = isOnboardingDone,
            theme = Theme.valueOf(theme),
            colorPalette = ColorPalettes.valueOf(colorPalettes),
            lastSetTime = lastSetTime?.let { Instant.fromEpochSeconds(it) }
        )
    }

    private companion object Keys {
        val ONBOARDING_DONE: Preferences.Key<Boolean> = booleanPreferencesKey("onboarding_done")
        val THEME: Preferences.Key<String> = stringPreferencesKey("theme")
        val COLOR_PALETTE: Preferences.Key<String> = stringPreferencesKey("color_palette")
        val LAST_SET_TIME_SECONDS: Preferences.Key<Long> =
            longPreferencesKey("last_set_time_seconds")
    }
}
