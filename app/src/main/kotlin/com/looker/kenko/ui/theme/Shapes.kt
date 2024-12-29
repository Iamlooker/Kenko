package com.looker.kenko.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

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

fun CornerBasedShape.end(
    end: CornerBasedShape,
): CornerBasedShape =
    copy(bottomEnd = end.bottomEnd, topEnd = end.topEnd)

fun CornerBasedShape.start(
    start: CornerBasedShape,
): CornerBasedShape =
    copy(bottomStart = start.bottomStart, topStart = start.topStart)
