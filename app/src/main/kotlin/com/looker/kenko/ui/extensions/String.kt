package com.looker.kenko.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun normalizeInt(value: Int, padding: Char = '0', length: Int = 2): String {
    return remember(value) {
        value.toString().padStart(length, padding)
    }
}