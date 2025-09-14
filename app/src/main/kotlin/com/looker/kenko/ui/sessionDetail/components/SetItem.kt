/*
 * Copyright (C) 2025. LooKeR & Contributors
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

package com.looker.kenko.ui.sessionDetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.looker.kenko.R
import com.looker.kenko.data.local.model.SetType
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.ExercisesPreviewParameter
import com.looker.kenko.data.model.RIR
import com.looker.kenko.data.model.RPE
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.repDurationStringRes
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.numbers

@Composable
fun SetItem(
    set: Set,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .heightIn(64.dp)
            .widthIn(240.dp, 420.dp)
            .background(MaterialTheme.colorScheme.surface)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.outline,
            LocalTextStyle provides MaterialTheme.typography.displayMedium.numbers(),
        ) {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                title()
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Row(
            modifier = Modifier
                .weight(1F)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(vertical = 16.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            PerformedItem(
                title = stringResource(set.exercise.repDurationStringRes),
                performance = "${set.repsOrDuration}",
            )
            PerformedItem(
                title = stringResource(R.string.label_weight),
                performance = "${set.weight} KG",
            )
        }
    }
}

@Composable
private fun PerformedItem(
    title: String,
    performance: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.outline,
        )
        Text(
            text = performance,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@PreviewScreenSizes
@Composable
private fun SetItemPreview(
    @PreviewParameter(ExercisesPreviewParameter::class, limit = 2) exercises: List<Exercise>,
) {
    KenkoTheme {
        SetItem(
            Set(12, 40F, SetType.Drop, exercises.first(), RPE(8), RIR(2)),
        ) {
            Text(text = "01")
        }
    }
}
