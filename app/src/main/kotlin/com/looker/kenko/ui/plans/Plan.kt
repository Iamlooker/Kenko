package com.looker.kenko.ui.plans

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.data.model.Plan
import com.looker.kenko.ui.helper.plus
import com.looker.kenko.ui.plans.components.PlanItem
import com.looker.kenko.ui.theme.KenkoIcons

@Composable
fun Plan(
    viewModel: PlanViewModel,
    onNavigateToAddPage: (Long?) -> Unit,
) {
    val plans: List<Plan> by viewModel.plans.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToAddPage(null) }) {
                Icon(imageVector = KenkoIcons.Add, contentDescription = null)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        LazyColumn(
            contentPadding = it + PaddingValues(bottom = 80.dp),
        ) {
            items(items = plans) { plan ->
                PlanItem(
                    plan = plan,
                    onClick = { onNavigateToAddPage(plan.id) },
                    onActiveChange = { viewModel.switchPlan(plan) },
                )
                HorizontalDivider()
            }
        }
    }
}