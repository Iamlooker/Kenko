package com.looker.kenko.ui.components.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

fun icon(
    name: String,
    size: Pair<Float, Float>,
    autoMirror: Boolean = false,
    block: ImageVector.Builder.() -> ImageVector.Builder,
): ImageVector {
    return ImageVector.Builder(
        name = name,
        defaultWidth = size.first.dp,
        defaultHeight = size.second.dp,
        viewportWidth = size.first,
        viewportHeight = size.second,
        autoMirror = autoMirror
    ).block().build()
}