package com.looker.kenko.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DayOfWeek

@Composable
fun HorizontalDaySelector(
    item: @Composable (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        items(DayOfWeek.entries) {
            item(it)
        }
    }
}

@Composable
fun DaySelectorChip(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val transition = updateTransition(selected, label = "Day Selector")
    val background by transition.animateColor(label = "Color") {
        if (it) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainer
        }
    }
    val corner by transition.animateDp(label = "Corner") {
        if (it) {
            28.dp
        } else {
            12.dp
        }
    }
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .graphicsLayer {
                clip = true
                shape = RoundedCornerShape(corner)
            }
            .drawBehind {
                drawRect(color = background)
            }
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.labelLarge,
            LocalContentColor provides MaterialTheme.colorScheme.onSurface,
            content = content,
        )
    }
}
