package com.looker.kenko.ui.plans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.model.Plan
import com.looker.kenko.ui.components.BackButton
import com.looker.kenko.ui.components.KenkoBorderWidth
import com.looker.kenko.ui.components.SwipeToDeleteBox
import com.looker.kenko.ui.extensions.plus
import com.looker.kenko.ui.planEdit.components.KenkoAddButton
import com.looker.kenko.ui.plans.components.PlanItem
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun Plan(
    viewModel: PlanViewModel,
    onBackPress: () -> Unit,
    onPlanClick: (Int) -> Unit,
) {
    val plans: List<Plan> by viewModel.plans.collectAsStateWithLifecycle()

    Plan(
        plans = plans,
        onBackPress = onBackPress,
        onSelectPlan = viewModel::switchPlan,
        onRemove = viewModel::removePlan,
        onPlanClick = onPlanClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Plan(
    plans: List<Plan>,
    onBackPress: () -> Unit,
    onSelectPlan: (Plan) -> Unit,
    onRemove: (Int) -> Unit,
    onPlanClick: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackButton(onClick = onBackPress)
                },
                title = {
                    Text(text = stringResource(R.string.label_plans_title))
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            KenkoAddButton(onClick = { onPlanClick(-1) })
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        LazyColumn(
            contentPadding = it + PaddingValues(bottom = 80.dp),
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
                    onDismiss = { onRemove(plan.id!!) }
                ) {
                    PlanItem(
                        plan = plan,
                        onClick = { onPlanClick(plan.id!!) },
                        onActiveChange = { onSelectPlan(plan) },
                    )
                }
                if (!isLast) HorizontalDivider(thickness = KenkoBorderWidth)
            }
        }
    }
}

@Preview
@Composable
private fun PlanPreview() {
    KenkoTheme {
        Plan(
            plans = listOf(
                Plan(
                    id = 1,
                    name = "Push Pull Leg",
                    isActive = true
                ),
                Plan(
                    id = 2,
                    name = "Push Pull",
                    isActive = false
                ),
            ),
            onSelectPlan = {},
            onBackPress = {},
            onPlanClick = {},
            onRemove = {},
        )
    }
}
