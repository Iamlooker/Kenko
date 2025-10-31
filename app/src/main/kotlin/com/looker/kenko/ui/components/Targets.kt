/*
 * Copyright (C) 2025 LooKeR & Contributors
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.looker.kenko.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.ui.exercises.string
import com.looker.kenko.ui.theme.KenkoTheme

private val SortedTargets = MuscleGroups.entries.sortedBy { it.string }
private val Targets = listOf(null) + SortedTargets

@Composable
fun FlowTargets(
    modifier: Modifier = Modifier,
    content: @Composable (MuscleGroups) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        SortedTargets.forEach { target ->
            content(target)
        }
    }
}

@Composable
fun LazyTargets(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable (MuscleGroups?) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        items(Targets) { target ->
            content(target)
        }
    }
}

@Composable
fun TargetChip(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text = text) },
        modifier = modifier,
    )
}

@Composable
fun HorizontalTargetChips(
    target: MuscleGroups?,
    onSelect: (MuscleGroups?) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(bottom = 4.dp),
    ) {
        Spacer(modifier = Modifier.width(contentPadding.calculateStartPadding(LocalLayoutDirection.current)))
        val sortedTargets = remember { Targets.sortedBy { it?.string } }
        sortedTargets.forEachIndexed { index, muscle ->
            FilterChip(
                selected = target == muscle,
                onClick = { onSelect(muscle) },
                label = { Text(text = stringResource(muscle.string)) },
            )
            if (sortedTargets.lastIndex != index) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Spacer(modifier = Modifier.width(contentPadding.calculateEndPadding(LocalLayoutDirection.current)))
    }
}

@Preview(showBackground = true)
@Composable
private fun HorizontalTargetChipsPreview() {
    KenkoTheme {
        LazyTargets {
            TargetChip(
                selected = it == null,
                onClick = {},
                text = stringResource(it.string),
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlowTargetChipsPreview() {
    KenkoTheme {
        FlowTargets {
            TargetChip(
                selected = false,
                onClick = {},
                text = stringResource(it.stringRes),
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        }
    }
}
