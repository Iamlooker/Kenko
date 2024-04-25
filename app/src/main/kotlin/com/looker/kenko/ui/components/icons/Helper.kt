package com.looker.kenko.ui.components.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun icon(
    name: String,
    viewPort: Pair<Float, Float>,
    size: Pair<Dp, Dp> = viewPort.first.dp to viewPort.second.dp,
    autoMirror: Boolean = false,
    block: ImageVector.Builder.() -> ImageVector.Builder,
): ImageVector {
    return ImageVector.Builder(
        name = name,
        defaultWidth = size.first,
        defaultHeight = size.second,
        viewportWidth = viewPort.first,
        viewportHeight = viewPort.second,
        autoMirror = autoMirror
    ).block().build()
}