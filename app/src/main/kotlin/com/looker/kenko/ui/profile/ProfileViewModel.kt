package com.looker.kenko.ui.profile

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.model.PlanStat
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
    planRepo: PlanRepo,
    sessionRepo: SessionRepo,
    exerciseRepo: ExerciseRepo,
) : ViewModel() {

    private val currentPlan: Flow<Plan?> = planRepo.current

    private val sets: Flow<Int> = sessionRepo.stream.map {
        it.flatMap { session -> session.sets }
    }.map { it.size }

    private val numberOfExercises: Flow<Int> = exerciseRepo.numberOfExercise

    val state: StateFlow<ProfileUiState> = combine(
        currentPlan,
        sets,
        numberOfExercises,
    ) { plan, sets, number ->
        ProfileUiState(
            numberOfExercises = number,
            totalLifts = sets,
            isPlanAvailable = plan != null,
            planName = plan?.name ?: "",
            planStat = plan?.stat,
        )
    }
        .asStateFlow(
            ProfileUiState(
                numberOfExercises = 0,
                isPlanAvailable = false,
                planName = "",
                totalLifts = 0,
                planStat = null,
            ),
        )

}

@Stable
data class ProfileUiState(
    val numberOfExercises: Int,
    val isPlanAvailable: Boolean,
    val planName: String,
    val totalLifts: Int,
    val planStat: PlanStat?,
)
