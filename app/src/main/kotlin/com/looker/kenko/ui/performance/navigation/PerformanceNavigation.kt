package com.looker.kenko.ui.performance.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.looker.kenko.ui.performance.Performance

const val PERFORMANCE_ROUTE = "performance"

fun NavGraphBuilder.performance() {
    composable(
        route = PERFORMANCE_ROUTE,
    ) {
        Performance()
    }
}
