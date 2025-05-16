/*
 * Copyright (C) 2025. LooKeR & Contributors
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.looker.kenko.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.looker.kenko.R
import com.looker.kenko.ui.home.navigation.HomeRoute
import com.looker.kenko.ui.performance.navigation.PerformanceRoute
import com.looker.kenko.ui.profile.navigation.ProfileRoute

// Manually add 80.dp padding for bottom app bar
enum class TopLevelDestinations(
    @StringRes val labelRes: Int,
    @DrawableRes val icon: Int,
    val route: Any,
) {
    Performance(R.string.label_performance, R.drawable.ic_show_chart, PerformanceRoute),
    Home(R.string.label_home, R.drawable.ic_home, HomeRoute),
    Profile(R.string.label_profile, R.drawable.ic_radio_button_unchecked, ProfileRoute),
}
