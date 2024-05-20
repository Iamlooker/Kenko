package com.looker.kenko.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.looker.kenko.R
import com.looker.kenko.ui.home.navigation.HomeRoute
import com.looker.kenko.ui.performance.navigation.PerformanceRoute
import com.looker.kenko.ui.profile.navigation.ProfileRoute
import com.looker.kenko.ui.theme.KenkoIcons

// Manually add 80.dp padding for bottom app bar
enum class TopLevelDestinations(
    @StringRes val labelRes: Int,
    val icon: ImageVector,
    val route: Any,
) {
    Performance(R.string.label_performance, KenkoIcons.Performance, PerformanceRoute),
    Home(R.string.label_home, KenkoIcons.Home, HomeRoute),
    Profile(R.string.label_profile, KenkoIcons.Circle, ProfileRoute),
}
