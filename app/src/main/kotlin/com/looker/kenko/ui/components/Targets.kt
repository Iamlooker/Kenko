package com.looker.kenko.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.ui.exercises.string

val Targets = listOf(null) + MuscleGroups.entries

@Composable
fun HorizontalTargetChips(
    target: MuscleGroups?,
    onSelect: (MuscleGroups?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Targets.forEach { muscle ->
            FilterChip(
                selected = target == muscle,
                onClick = { onSelect(muscle) },
                label = { Text(text = stringResource(muscle.string)) }
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowHorizontalChips(
    target: MuscleGroups,
    onSet: (MuscleGroups) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MuscleGroups.entries.forEach { muscle ->
            FilterChip(
                selected = target == muscle,
                onClick = { onSet(muscle) },
                label = { Text(text = stringResource(muscle.stringRes)) }
            )
        }
    }
}
