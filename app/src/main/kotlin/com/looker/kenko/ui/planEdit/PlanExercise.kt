package com.looker.kenko.ui.planEdit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.looker.kenko.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlanExercise(
    header: @Composable () -> Unit,
    items: LazyListScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        stickyHeader {
            header()
        }
        items()
    }
}

@Composable
fun Header(
    isExpandedView: Boolean,
    modifier: Modifier = Modifier,
    daySelector: @Composable () -> Unit,
    daySwitcher: @Composable () -> Unit,
) {
    Column {
        Text(
            text = stringResource(R.string.heading_select_plan_items),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.secondary,
        )
        Spacer(Modifier.height(8.dp))
        AnimatedContent(
            modifier = modifier.background(MaterialTheme.colorScheme.surface),
            targetState = isExpandedView,
            label = "Header",
            transitionSpec = {
                if (targetState) {
                    slideInVertically { it / 3 } + fadeIn() togetherWith slideOutVertically { -it / 3 } + fadeOut()
                } else {
                    slideInVertically { -it / 3 } + fadeIn() togetherWith slideOutVertically { it / 3 } + fadeOut()
                } using SizeTransform(clip = false)
            },
            contentAlignment = Alignment.Center,
        ) { expanded ->
            if (expanded) {
                daySelector()
            } else {
                daySwitcher()
            }
        }
    }
}
