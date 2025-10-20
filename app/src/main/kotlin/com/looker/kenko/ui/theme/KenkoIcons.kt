/*
 * Copyright (C) 2025 LooKeR & Contributors
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

package com.looker.kenko.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.looker.kenko.R

/**
 * Material Symbols Settings
 *
 * Weight: 300
 * Grade: 0
 * Optical Size: 24px
 * Style: Rounded
 * Fill: False
 */
object KenkoIcons {

    val ArrowBack: Painter
        @Composable
        get() = painterResource(R.drawable.ic_arrow_back)

    val ArrowForward: Painter
        @Composable
        get() = painterResource(R.drawable.ic_arrow_forward)

    val ArrowOutward: Painter
        @Composable
        get() = painterResource(R.drawable.ic_arrow_outward)

    val Circle: Painter
        @Composable
        get() = painterResource(R.drawable.ic_radio_button_unchecked)

    val Lightbulb: Painter
        @Composable
        get() = painterResource(R.drawable.ic_lightbulb)

    val Add: Painter
        @Composable
        get() = painterResource(R.drawable.ic_add)

    val Info: Painter
        @Composable
        get() = painterResource(R.drawable.ic_info)

    val Done: Painter
        @Composable
        get() = painterResource(R.drawable.ic_check)

    val History: Painter
        @Composable
        get() = painterResource(R.drawable.ic_history)

    val Delete: Painter
        @Composable
        get() = painterResource(R.drawable.ic_delete)

    val Remove: Painter
        @Composable
        get() = painterResource(R.drawable.ic_remove)

    val Save: Painter
        @Composable
        get() = painterResource(R.drawable.ic_save)

    val Rename: Painter
        @Composable
        get() = painterResource(R.drawable.ic_edit)

    val Plan: Painter
        @Composable
        get() = painterResource(R.drawable.ic_tactic)

    val Home: Painter
        @Composable
        get() = painterResource(R.drawable.ic_home)

    val Performance: Painter
        @Composable
        get() = painterResource(R.drawable.ic_show_chart)

    val Settings: Painter
        @Composable
        get() = painterResource(R.drawable.ic_settings)

    val KeyboardArrowRight: Painter
        @Composable
        get() = painterResource(R.drawable.ic_keyboard_arrow_right)

    val KeyboardArrowLeft: Painter
        @Composable
        get() = painterResource(R.drawable.ic_keyboard_arrow_left)

    // Brutalist Icons
    val AddLarge: ImageVector = com.looker.kenko.ui.components.icons.AddLarge

    val ArrowOutwardLarge: ImageVector = com.looker.kenko.ui.components.icons.ArrowOutwardLarge

    val Cloud: ImageVector = com.looker.kenko.ui.components.icons.Cloud

    val Colony: ImageVector = com.looker.kenko.ui.components.icons.Colony

    val Arrow1: ImageVector = com.looker.kenko.ui.components.icons.Arrow1

    val Arrow2: ImageVector = com.looker.kenko.ui.components.icons.Arrow2

    val Arrow3: ImageVector = com.looker.kenko.ui.components.icons.Arrow3

    val Arrow4: ImageVector = com.looker.kenko.ui.components.icons.Arrow4

    val Dawn: ImageVector = com.looker.kenko.ui.components.icons.Dawn

    val ConcentricTriangles: ImageVector = com.looker.kenko.ui.components.icons.ConcentricTriangles

    val Stack: ImageVector = com.looker.kenko.ui.components.icons.Stack

    val Reveal: ImageVector = com.looker.kenko.ui.components.icons.Reveal

    val QuarterCircles: ImageVector = com.looker.kenko.ui.components.icons.QuarterCircles

    val Wireframe: ImageVector = com.looker.kenko.ui.components.icons.Wireframe
}
