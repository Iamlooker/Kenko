package com.looker.kenko.ui.profile

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    repo: ExerciseRepo,
    planRepo: PlanRepo,
    sessionRepo: SessionRepo,
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
        .asStateFlow(
            ProfileUiState(
                numberOfExercises = 0,
                numberOfExercisesPerPlan = 0,
                isPlanAvailable = false,
                planName = "",
                restDays = 0,
                workDays = 0,
                totalLifts = 0
            )
        )

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
