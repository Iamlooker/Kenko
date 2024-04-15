package com.looker.kenko.ui.plans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.data.model.Plan
import com.looker.kenko.ui.plans.components.PlanItem

@Composable
fun Plan(
    viewModel: PlanViewModel,
    onNavigateToAddPage: (Long?) -> Unit,
) {
    val plans: List<Plan> by viewModel.plans.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.add() }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
            }
        },
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = it,
        ) {
            items(items = plans) { plan ->
                PlanItem(
                    plan = plan,
                    modifier = Modifier.padding(horizontal = 14.dp),
                )
            }
        }
    }
}