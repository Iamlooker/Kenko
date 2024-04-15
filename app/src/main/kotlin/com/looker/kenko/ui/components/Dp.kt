package com.looker.kenko.ui.components

import androidx.compose.ui.unit.Dp

data class DpRange(
    val start: Dp,
    val end: Dp
)

operator fun Dp.rangeTo(other: Dp): DpRange = DpRange(this, other)