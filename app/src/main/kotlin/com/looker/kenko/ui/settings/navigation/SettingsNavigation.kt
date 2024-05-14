package com.looker.kenko.ui.settings.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.ui.settings.Settings

const val SETTINGS_ROUTE = "settings"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    navigate(SETTINGS_ROUTE, navOptions)
}

fun NavGraphBuilder.settings(
    onBackPress: () -> Unit,
) {
    composable(
        route = SETTINGS_ROUTE
    ) {
        Settings(
            onBackPress = onBackPress,
            viewModel = hiltViewModel(),
        )
    }
}
