package com.looker.kenko.ui.planEdit.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.kenko.R
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.end
import com.looker.kenko.ui.theme.start
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
fun DaySwitcher(
    selected: DayOfWeek,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.widthIn(max = 400.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        val buttonColors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
        Button(
            modifier = Modifier.height(56.dp),
            shape = MaterialTheme.shapes.large.end(MaterialTheme.shapes.small),
            colors = buttonColors,
            onClick = onPrevious,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .height(56.dp)
                .weight(1F)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
            content = {
                AnimatedContent(
                    targetState = selected,
                    label = "",
                    transitionSpec = {
                        fadeAndSlideHorizontally(
                            (initialState == SUNDAY && targetState == MONDAY) ||
                                (targetState > initialState && !(initialState == MONDAY && targetState == SUNDAY))
                        ) using SizeTransform(clip = false)
                    },
                ) { day ->
                    Text(
                        text = kenkoDayName(dayOfWeek = day),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            },
        )
        Spacer(modifier = Modifier.width(6.dp))
        Button(
            modifier = Modifier.height(56.dp),
            shape = MaterialTheme.shapes.large.start(MaterialTheme.shapes.small),
            colors = buttonColors,
            onClick = onNext,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

fun fadeAndSlideHorizontally(rightToLeft: Boolean = true): ContentTransform {
    return slideInHorizontally { it * if (rightToLeft) 1 else -1 } + fadeIn() togetherWith
        slideOutHorizontally { -it * if (rightToLeft) 1 else -1 } + fadeOut()
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
        DaySwitcher(
            onNext = {
                isSelected += 1
            },
            onPrevious = {
                isSelected -= 1
            },
            onClick = {
            },
            selected = isSelected,
        )
    }
}
