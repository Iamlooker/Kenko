package com.looker.kenko.ui.plans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.data.repository.SettingsRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

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
}