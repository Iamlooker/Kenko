package com.looker.kenko.ui.addSet

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.SetType
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.ui.addSet.components.DragEvents
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
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
        reps.setTextAndPlaceCursorAtEnd((repInt + value).toString())
    }

    fun addWeight(value: Double) {
        weights.setTextAndPlaceCursorAtEnd((weightDouble + value).toString())
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
            val exercise = exerciseRepo.get(exerciseName) ?: return@launch
            val set = Set(
                repsOrDuration = repInt,
                weight = weightDouble,
                exercise = exercise,
                type = SetType.Standard
            )
            sessionRepo.addSet(localDate, set)
        }
    }

    private inline val repInt: Int
        get() = reps.text.toString().toIntOrNull() ?: 0

    private inline val weightDouble: Double
        get() = weights.text.toString().toDoubleOrNull() ?: 0.0

    @AssistedFactory
    interface AddSetViewModelFactory {
        fun create(name: String): AddSetViewModel
    }

    object IntTransformation : InputTransformation {
        override val keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        override fun TextFieldBuffer.transformInput() {
            if (!asCharSequence().isDigitsOnly()) {
                revertAllChanges()
            }
        }
    }

    object DoubleTransformation : InputTransformation {
        override val keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        override fun TextFieldBuffer.transformInput() {
            toString().toDoubleOrNull() ?: revertAllChanges()
        }
    }
}
