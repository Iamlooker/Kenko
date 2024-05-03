package com.looker.kenko.ui.planEdit.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.kenko.R
import com.looker.kenko.ui.theme.KenkoTheme
import kotlinx.datetime.DayOfWeek
import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.SUNDAY
import java.time.DayOfWeek.THURSDAY
import java.time.DayOfWeek.TUESDAY
import java.time.DayOfWeek.WEDNESDAY
import java.time.format.TextStyle

@Composable
fun DaySelector(
    dayItem: @Composable (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.weight(1F))
        DayOfWeek.entries.forEach {
            dayItem(it)
            if (it != SUNDAY) {
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
        Spacer(modifier = Modifier.weight(1F))
    }
}

@Composable
fun DayItem(
    dayOfWeek: DayOfWeek,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectionTransition = updateTransition(targetState = isSelected, label = "$dayOfWeek")
    val selectionBackground by selectionTransition.animateColor(label = "Color") { selected ->
        if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surface
        }
    }
    val selectionOnBackground by selectionTransition.animateColor(label = "OnColor") { selected ->
        if (selected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    }
    val selectionPadding by selectionTransition.animateDp(label = "Padding") { selected ->
        if (selected) {
            8.dp
        } else {
            0.dp
        }
    }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(CircleShape)
            .drawBehind { drawRect(selectionBackground) }
            .padding(selectionPadding)
            .clickable(onClick = onClick)
            .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = kenkoDayName(dayOfWeek = dayOfWeek),
            color = selectionOnBackground,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
fun kenkoDayName(dayOfWeek: DayOfWeek): String {
    val dayId = remember(dayOfWeek) {
        when (dayOfWeek) {
            MONDAY -> R.string.label_monday
            TUESDAY -> R.string.label_tuesday
            WEDNESDAY -> R.string.label_wednesday
            THURSDAY -> R.string.label_thursday
            FRIDAY -> R.string.label_friday
            SATURDAY -> R.string.label_saturday
            SUNDAY -> R.string.label_sunday
        }
    }
    return stringResource(dayId)
}

@Composable
fun dayName(dayOfWeek: DayOfWeek): String {
    val locale = remember { Locale.current.platformLocale }
    val format = remember { TextStyle.FULL }
    return remember(dayOfWeek) { dayOfWeek.getDisplayName(format, locale) }
}

@Preview
@Composable
private fun DaySelectorPreview() {
    KenkoTheme {
        var isSelected by remember {
            mutableStateOf(THURSDAY)
        }
        DaySelector(
            dayItem = {
                DayItem(
                    dayOfWeek = it,
                    isSelected = isSelected == it,
                    onClick = { isSelected = it })
            }
        )
    }
}
