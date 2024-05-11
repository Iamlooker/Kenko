package com.looker.kenko.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun CornerBasedShape.end(
    bottomEnd: Dp = 0.dp,
    topEnd: Dp = bottomEnd,
): CornerBasedShape =
    copy(bottomEnd = CornerSize(bottomEnd), topEnd = CornerSize(topEnd))

fun CornerBasedShape.start(
    bottomStart: Dp = 0.dp,
    topStart: Dp = bottomStart,
): CornerBasedShape =
    copy(bottomStart = CornerSize(bottomStart), topStart = CornerSize(topStart))
