package com.looker.kenko.ui.selectExercise

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectExerciseViewModel @Inject constructor(
    repo: ExerciseRepo,
) : ViewModel() {

    var searchQuery: String by mutableStateOf("")
        private set

    private val searchQueryFlow = snapshotFlow { searchQuery }

    private val exerciseStream = repo.stream

    private val _targetMuscle: MutableStateFlow<MuscleGroups?> = MutableStateFlow(null)
    val targetMuscle: StateFlow<MuscleGroups?> = _targetMuscle.asStateFlow()

    val searchResult = combine(
        searchQueryFlow,
        targetMuscle,
        exerciseStream,
    ) { query, target, exercises ->
        val filteredExercises = exercises
            .filter {
                (it.target == target || target == null) && it.satisfiesSearch(query)
            }
        if (filteredExercises.isNotEmpty()) {
            SearchResult.Success(filteredExercises)
        } else {
            SearchResult.NotFound
        }
    }.asStateFlow(SearchResult.Loading)

    fun setTarget(target: MuscleGroups?) {
        viewModelScope.launch {
            _targetMuscle.emit(target)
        }
    }

    fun setSearch(value: String) {
        searchQuery = value
    }

    private fun Exercise.satisfiesSearch(query: String): Boolean {
        return query.isBlank() || name.contains(query, ignoreCase = true)
    }
}

@Stable
sealed interface SearchResult {

    @Stable
    data object Loading : SearchResult

    @Stable
    data class Success(val exercises: List<Exercise>) : SearchResult

    @Stable
    data object NotFound : SearchResult

}
