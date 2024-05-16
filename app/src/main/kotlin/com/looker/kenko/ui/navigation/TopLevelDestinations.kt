package com.looker.kenko.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.looker.kenko.R
import com.looker.kenko.ui.theme.KenkoIcons

// Manually add 80.dp padding for bottom app bar
enum class TopLevelDestinations(
    @StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    Performance(R.string.label_performance, KenkoIcons.Performance),
    Home(R.string.label_home, KenkoIcons.Home),
    Profile(R.string.label_profile, KenkoIcons.Circle),
}
