package com.looker.kenko.ui.plans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.data.model.MuscleGroups.Biceps
import com.looker.kenko.data.model.MuscleGroups.Chest
import com.looker.kenko.data.model.MuscleGroups.Hamstrings
import com.looker.kenko.data.model.MuscleGroups.Quads
import com.looker.kenko.data.model.MuscleGroups.Shoulders
import com.looker.kenko.data.model.MuscleGroups.Triceps
import com.looker.kenko.data.model.MuscleGroups.UpperBack
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.model.sampleExercises
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val repo: PlanRepo,
) : ViewModel() {

    val plans = repo.stream.asStateFlow(emptyList())

    fun add() {
        viewModelScope.launch {
            repo.upsert(
                Plan(
                    name = "Chest Back Legs",
                    exercisesPerDay = mapOf(
                        DayOfWeek.MONDAY to Chest.sampleExercises + Triceps.sampleExercises,
                        DayOfWeek.TUESDAY to UpperBack.sampleExercises + Biceps.sampleExercises,
                        DayOfWeek.WEDNESDAY to Quads.sampleExercises + Hamstrings.sampleExercises
                                + Shoulders.sampleExercises,
                        DayOfWeek.THURSDAY to Chest.sampleExercises + Triceps.sampleExercises,
                        DayOfWeek.FRIDAY to UpperBack.sampleExercises + Biceps.sampleExercises,
                    ).mapValues { it.value.shuffled().take(5) },
                    isActive = true,
                )
            )
        }
    }
}