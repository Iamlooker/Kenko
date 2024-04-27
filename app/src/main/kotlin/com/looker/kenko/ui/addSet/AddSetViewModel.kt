package com.looker.kenko.ui.addSet

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.SetType
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.ui.components.draggableTextField.DragEvents
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

@HiltViewModel(assistedFactory = AddSetViewModel.AddSetViewModelFactory::class)
class AddSetViewModel @AssistedInject constructor(
    private val sessionRepo: SessionRepo,
    private val exerciseRepo: ExerciseRepo,
    @Assisted private val exerciseName: String,
) : ViewModel() {

    private var timer: Timer? = null

    val reps: TextFieldState = TextFieldState("12")
    val weights: TextFieldState = TextFieldState("20.0")

    fun addRep(value: Int) {
        val updatedRep = (getRep() ?: 0) + value
        reps.setTextAndPlaceCursorAtEnd(updatedRep.toString())
    }

    fun addWeight(value: Double) {
        val updatedWeight = (getWeight() ?: 0.0) + value
        weights.setTextAndPlaceCursorAtEnd(updatedWeight.toString())
    }

    val repsDragEvents: DragEvents = object : DragEvents {
        override fun onHold(isRight: Boolean) {
            onStop()
            timer = fixedRateTimer(
                name = "Rep Increment",
                initialDelay = 100L,
                period = 200L
            ) {
                addRep(if (isRight) 1 else -1)
            }
        }

        override fun onStop() {
            timer?.cancel()
            timer = null
        }
    }

    val weightsDragEvents: DragEvents = object : DragEvents {
        override fun onHold(isRight: Boolean) {
            onStop()
            timer = fixedRateTimer(
                name = "Weight Increment",
                initialDelay = 100L,
                period = 200L
            ) {
                addWeight(if (isRight) 1.0 else -1.0)
            }
        }

        override fun onStop() {
            timer?.cancel()
            timer = null
        }
    }

    fun addSet() {
        viewModelScope.launch {
            val exercise = async {
                exerciseRepo.get(exerciseName)
            }
            val reps = getRep() ?: return@launch
            val weights = getWeight() ?: return@launch
            val set = Set(
                repsOrDuration = reps,
                weight = weights,
                exercise = exercise.await() ?: return@launch,
                type = SetType.Standard
            )
            sessionRepo.addSet(localDate, set)
        }
    }

    private fun getRep(): Int? {
        return reps.text.toString().toIntOrNull()
    }

    private fun getWeight(): Double? {
        return weights.text.toString().toDoubleOrNull()
    }

    @AssistedFactory
    interface AddSetViewModelFactory {
        fun create(name: String): AddSetViewModel
    }
}