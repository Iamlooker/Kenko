package com.looker.kenko.ui.performance.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.ui.performance.Performance
import kotlinx.serialization.Serializable

@Serializable
object PerformanceRoute

fun NavController.navigateToPerformance(navOptions: NavOptions? = null) {
    navigate(PerformanceRoute, navOptions)
}

fun NavGraphBuilder.performance() {
    composable<PerformanceRoute> {
        Performance()
    }
}
