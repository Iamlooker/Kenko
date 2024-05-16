package com.looker.kenko.ui.plans

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.model.Plan
import com.looker.kenko.ui.helper.plus
import com.looker.kenko.ui.plans.components.PlanItem
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun Plan(
    viewModel: PlanViewModel,
    onPlanClick: (Long?) -> Unit,
) {
    val plans: List<Plan> by viewModel.plans.collectAsStateWithLifecycle()

    Plan(
        plans = plans,
        onSelectPlan = viewModel::switchPlan,
        onPlanClick = onPlanClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Plan(
    plans: List<Plan>,
    onSelectPlan: (Plan) -> Unit,
    onPlanClick: (Long?) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.label_plans_title))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onPlanClick(null) }) {
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
                    onClick = { onPlanClick(plan.id!!) },
                    onActiveChange = { onSelectPlan(plan) },
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview
@Composable
private fun PlanPreview() {
    KenkoTheme {
        Plan(
            plans = emptyList(),
            onSelectPlan = {},
            onPlanClick = {}
        )
    }
}
