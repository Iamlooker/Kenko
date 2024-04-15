package com.looker.kenko.ui.addSet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Set
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

@OptIn(ExperimentalFoundationApi::class)
@HiltViewModel
class AddSetViewModel @Inject constructor(
) : ViewModel() {

    private var timer: Timer? = null

    private var exercise: Exercise? = null

    fun exercise(exercise: Exercise?) {
        this.exercise = exercise
    }

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

    fun onHoldRepIncrement(isRight: Boolean) {
        onStopIncrement()
        timer = fixedRateTimer(
            name = "Rep Increment",
            initialDelay = 100L,
            period = 200L
        ) {
            addRep(if (isRight) 1 else -1)
        }
    }

    fun onHoldWeightIncrement(isRight: Boolean) {
        onStopIncrement()
        timer = fixedRateTimer(
            name = "Weight Increment",
            initialDelay = 100L,
            period = 200L
        ) {
            addWeight(if (isRight) 1.0 else -1.0)
        }
    }

    fun onStopIncrement() {
        timer?.cancel()
        timer = null
    }

    fun addSet(onDone: (Set) -> Unit) {
        viewModelScope.launch {
            onDone(
                Set(
                    repsOrDuration = getRep() ?: return@launch,
                    weight = getWeight() ?: return@launch,
                    exercise = exercise ?: return@launch
                )
            )
        }
    }

    private fun getRep(): Int? {
        return reps.text.toString().toIntOrNull()
    }

    private fun getWeight(): Double? {
        return weights.text.toString().toDoubleOrNull()
    }
}