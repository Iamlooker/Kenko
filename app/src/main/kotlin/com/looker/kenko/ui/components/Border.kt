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