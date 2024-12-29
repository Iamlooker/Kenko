package com.looker.kenko.data.repository

import com.looker.kenko.data.model.settings.ColorPalettes
import com.looker.kenko.data.model.settings.Settings
import com.looker.kenko.data.model.settings.Theme
import kotlinx.coroutines.flow.Flow

interface SettingsRepo {

    val stream: Flow<Settings>

    fun <T> get(block: Settings.() -> T): Flow<T>

    suspend fun setOnboardingDone()

    suspend fun setColorPalette(colorPalette: ColorPalettes)

    suspend fun setTheme(theme: Theme)

}
