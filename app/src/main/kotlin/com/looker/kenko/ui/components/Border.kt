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

package com.looker.kenko.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val KenkoBorderWidth: Dp = 1.4.dp

val PrimaryBorder: BorderStroke
    @Composable
    get() = BorderStroke(KenkoBorderWidth, MaterialTheme.colorScheme.primary)

val SecondaryBorder: BorderStroke
    @Composable
    get() = BorderStroke(KenkoBorderWidth, MaterialTheme.colorScheme.secondary)

val OutlineBorder: BorderStroke
    @Composable
    get() = BorderStroke(KenkoBorderWidth, MaterialTheme.colorScheme.secondary)

val OnSurfaceBorder: BorderStroke
    @Composable
    get() = BorderStroke(KenkoBorderWidth, MaterialTheme.colorScheme.onSurface)

val OnSurfaceVariantBorder: BorderStroke
    @Composable
    get() = BorderStroke(KenkoBorderWidth, MaterialTheme.colorScheme.onSurfaceVariant)
