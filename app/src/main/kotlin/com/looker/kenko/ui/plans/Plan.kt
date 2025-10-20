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

package com.looker.kenko.ui.plans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.model.PlanPreviewParameters
import com.looker.kenko.ui.components.BackButton
import com.looker.kenko.ui.components.KenkoBorderWidth
import com.looker.kenko.ui.components.SwipeToDeleteBox
import com.looker.kenko.ui.components.endItem
import com.looker.kenko.ui.planEdit.components.KenkoAddButton
import com.looker.kenko.ui.plans.components.PlanItem
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun Plan(
    viewModel: PlanViewModel,
    onBackPress: () -> Unit,
    onPlanClick: (Int) -> Unit,
) {
    var showHelpDialog by remember { mutableStateOf(false) }
    val plans: List<Plan> by viewModel.plans.collectAsStateWithLifecycle()

    Plan(
        plans = plans,
        onBackPress = onBackPress,
        onInfoClick = { showHelpDialog = true },
        onSelectPlan = viewModel::switchPlan,
        onRemove = viewModel::removePlan,
        onPlanClick = onPlanClick,
    )

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text(text = stringResource(R.string.label_clean_up)) },
            text = { Text(text = stringResource(R.string.label_clean_up_plans)) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.cleanupPlans { showHelpDialog = false }
                    },
                ) {
                    Text(text = stringResource(R.string.label_yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text(text = stringResource(R.string.label_no))
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Plan(
    plans: List<Plan>,
    onBackPress: () -> Unit,
    onInfoClick: () -> Unit,
    onSelectPlan: (Plan) -> Unit,
    onRemove: (Int) -> Unit,
    onPlanClick: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { BackButton(onClick = onBackPress) },
                title = { Text(text = stringResource(R.string.label_plans_title)) },
                actions = {
                    IconButton(onClick = onInfoClick) {
                        Icon(
                            painter = KenkoIcons.Info,
                            contentDescription = "Info"
                        )
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = { KenkoAddButton(onClick = { onPlanClick(-1) }) },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        LazyColumn(
            contentPadding = it,
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            items(
                items = plans,
                key = { plan -> plan.id!! },
            ) { plan ->
                val isLast = remember(plan) {
                    plans.last() == plan
                }
                SwipeToDeleteBox(
                    modifier = Modifier.animateItem(),
                    onDismiss = { onRemove(plan.id!!) },
                ) {
                    PlanItem(
                        plan = plan,
                        onClick = { onPlanClick(plan.id!!) },
                        onActiveChange = { onSelectPlan(plan) },
                    )
                }
                if (!isLast) HorizontalDivider(thickness = KenkoBorderWidth)
            }
            endItem()
        }
    }
}

@Preview
@Composable
private fun PlanPreview(
    @PreviewParameter(PlanPreviewParameters::class) plans: List<Plan>,
) {
    KenkoTheme {
        Plan(
            plans = plans,
            onSelectPlan = {},
            onInfoClick = {},
            onBackPress = {},
            onPlanClick = {},
            onRemove = {},
        )
    }
}
