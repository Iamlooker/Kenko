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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.data.repository.SettingsRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val repo: PlanRepo,
    private val settingsRepo: SettingsRepo,
) : ViewModel() {

    val plans = repo.plans.asStateFlow(emptyList())

    fun removePlan(id: Int) {
        viewModelScope.launch {
            repo.deletePlan(id)
        }
    }

    fun switchPlan(plan: Plan) {
        viewModelScope.launch {
            if (!plan.isActive) {
                repo.setCurrent(plan.id!!)
            } else {
                repo.updatePlan(plan.copy(isActive = false))
            }
            if (repo.current.first() != null) {
                settingsRepo.setOnboardingDone()
            }
        }
    }

    fun cleanupPlans(onDone: () -> Unit) {
        viewModelScope.launch {
            repo.deleteEmptyPlans()
            onDone()
        }
    }
}
