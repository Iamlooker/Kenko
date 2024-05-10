package com.looker.kenko.ui.profile

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.BuildConfig
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.data.repository.SettingsRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    repo: ExerciseRepo,
    planRepo: PlanRepo,
    sessionRepo: SessionRepo,
    private val settingsRepo: SettingsRepo,
) : ViewModel() {

    private val exercisesSizeStream: Flow<Int> = repo.stream.map { it.size }

    private val currentPlan: Flow<Plan?> = planRepo.current

    private val sets: Flow<Int> = sessionRepo.stream.map {
        it.flatMap { session -> session.sets }
    }.map { it.size }

    val state: StateFlow<ProfileUiState> = combine(
        exercisesSizeStream,
        currentPlan,
        sets,
    ) { numberOfExercises, plan, sets ->
        ProfileUiState(
            numberOfExercises = numberOfExercises,
            totalLifts = sets,
            isPlanAvailable = plan != null,
            planName = plan?.name ?: "",
            numberOfExercisesPerPlan = plan?.exercisesPerDay?.values?.flatten()?.size ?: 0,
            workDays = plan?.exercisesPerDay?.size ?: 0,
            restDays = 7 - (plan?.exercisesPerDay?.size ?: 0),
        )
    }
        .asStateFlow(ProfileUiState(0, 0, false, "", 0, 0, 0))

    fun completeOnboarding() {
        viewModelScope.launch {
            if (!BuildConfig.DEBUG) settingsRepo.setOnboardingDone()
        }
    }

}

@Stable
data class ProfileUiState(
    val numberOfExercises: Int,
    val numberOfExercisesPerPlan: Int,
    val isPlanAvailable: Boolean,
    val planName: String,
    val restDays: Int,
    val workDays: Int,
    val totalLifts: Int,
)
