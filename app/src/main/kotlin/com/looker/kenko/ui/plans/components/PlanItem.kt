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

package com.looker.kenko.ui.plans.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.looker.kenko.R
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.model.PlanPreviewParameters
import com.looker.kenko.ui.extensions.normalizeInt
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun PlanItem(
    plan: Plan,
    onActiveChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val transition = updateTransition(targetState = plan.isActive, label = null)
    val background by transition.animateColor(label = "background") {
        if (it) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainer
        }
    }
    val contentColor by transition.animateColor(label = "foreground") {
        if (it) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    }

    Surface(
        onClick = onClick,
        color = background,
        contentColor = contentColor,
    ) {
        Column(
            modifier = modifier.padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = plan.name, style = MaterialTheme.typography.headlineMedium)
                OutlinedIconToggleButton(
                    checked = plan.isActive,
                    onCheckedChange = onActiveChange,
                ) {
                    Icon(imageVector = KenkoIcons.Done, contentDescription = null)
                }
            }
            AnimatedVisibility(visible = plan.isActive) {
                Text(
                    text = ".selected",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
            if (plan.isActive) {
                Spacer(modifier = Modifier.height(4.dp))
            }
            val stats = remember(plan) { plan.stat }
            Text(
                text = stringResource(
                    R.string.label_plan_description,
                    stats.exercises,
                    normalizeInt(stats.workDays),
                    normalizeInt(7 - stats.workDays),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PlanItemPreview(@PreviewParameter(PlanPreviewParameters::class) plans: List<Plan>) {
    KenkoTheme {
        var plan by remember { mutableStateOf(plans.first()) }
        PlanItem(plan = plan, { plan = plan.copy(isActive = it) }, {})
    }
}

@PreviewLightDark
@Composable
private fun PlanItemInActivePreview(@PreviewParameter(PlanPreviewParameters::class) plans: List<Plan>) {
    KenkoTheme {
        PlanItem(plan = plans.first(), {}, {})
    }
}
