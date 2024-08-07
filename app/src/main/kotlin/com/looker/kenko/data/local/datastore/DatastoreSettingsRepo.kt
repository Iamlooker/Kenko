package com.looker.kenko.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.looker.kenko.BuildConfig
import com.looker.kenko.data.model.settings.ColorPalettes
import com.looker.kenko.data.model.settings.Settings
import com.looker.kenko.data.model.settings.Theme
import com.looker.kenko.data.repository.SettingsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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

    private suspend inline fun <T> Preferences.Key<T>.update(value: T) {
        dataStore.edit { preference ->
            preference[this] = value
        }
    }

    private fun mapSettings(preferences: Preferences): Settings {
        val isOnboardingDone = preferences[ONBOARDING_DONE] ?: false
        val theme = preferences[THEME] ?: Theme.System.name
        val colorPalettes = preferences[COLOR_PALETTE] ?: ColorPalettes.Default.name
        return Settings(
            isOnboardingDone = isOnboardingDone,
            theme = Theme.valueOf(theme),
            colorPalette = ColorPalettes.valueOf(colorPalettes),
        )
    }

    private companion object Keys {
        val ONBOARDING_DONE: Preferences.Key<Boolean> = booleanPreferencesKey("onboarding_done")
        val THEME: Preferences.Key<String> = stringPreferencesKey("theme")
        val COLOR_PALETTE: Preferences.Key<String> = stringPreferencesKey("color_palette")
    }
}
