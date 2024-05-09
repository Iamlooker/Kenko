package com.looker.kenko.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.looker.kenko.data.model.Settings
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

    override suspend fun setOnboardingDone() {
        ONBOARDING_DONE.update(true)
    }

    private suspend inline fun <T> Preferences.Key<T>.update(value: T) {
        dataStore.edit { preference ->
            preference[this] = value
        }
    }

    private fun mapSettings(preferences: Preferences): Settings {
        val isOnboardingDone = preferences[ONBOARDING_DONE] ?: false
        return Settings(
            isOnboardingDone = isOnboardingDone,
        )
    }

    private companion object Keys {
        val ONBOARDING_DONE: Preferences.Key<Boolean> = booleanPreferencesKey("onboarding_done")
    }

}