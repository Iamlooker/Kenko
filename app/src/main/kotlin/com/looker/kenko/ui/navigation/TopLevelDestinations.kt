package com.looker.kenko.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChangeHistory
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.looker.kenko.R

enum class TopLevelDestinations(
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    Home(R.string.label_home, Icons.Rounded.Circle),
    Performance(R.string.label_performance, Icons.Rounded.ChangeHistory),
    Profile(R.string.label_profile, Icons.Rounded.Person),
}