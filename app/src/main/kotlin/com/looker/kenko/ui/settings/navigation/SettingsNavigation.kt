package com.looker.kenko.ui.settings.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.ui.settings.Settings
import kotlinx.serialization.Serializable

@Serializable
object SettingsRoute

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    navigate(SettingsRoute, navOptions)
}

fun NavGraphBuilder.settings(
    onBackPress: () -> Unit,
) {
    composable<SettingsRoute> {
        Settings(
            onBackPress = onBackPress,
            viewModel = hiltViewModel(),
        )
    }
}
