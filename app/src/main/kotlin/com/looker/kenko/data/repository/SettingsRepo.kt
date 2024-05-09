package com.looker.kenko.data.repository

import com.looker.kenko.data.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepo {

    val stream: Flow<Settings>

    suspend fun setOnboardingDone()

}