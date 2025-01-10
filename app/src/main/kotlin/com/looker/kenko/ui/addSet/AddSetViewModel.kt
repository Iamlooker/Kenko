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
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.data.repository.SessionRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AddSetViewModel.AddSetViewModelFactory::class)
class AddSetViewModel @AssistedInject constructor(
    private val sessionRepo: SessionRepo,
    private val exerciseRepo: ExerciseRepo,
    @Assisted private val id: Int,
) : ViewModel() {

    val reps: TextFieldState = TextFieldState("12")
    val weights: TextFieldState = TextFieldState("20.0")

    fun addRep(value: Int) {
        reps.setTextAndPlaceCursorAtEnd((repInt + value).toString())
    }

    fun addWeight(value: Float) {
        weights.setTextAndPlaceCursorAtEnd((weightDouble + value).toString())
    }

    fun addSet() {
        viewModelScope.launch {
            val exercise = exerciseRepo.get(id) ?: return@launch
            val set = Set(
                repsOrDuration = repInt,
                weight = weightDouble,
                exercise = exercise,
                type = SetType.Standard,
            )
            sessionRepo.addSet(set)
        }
    }

    private inline val repInt: Int
        get() = reps.text.toString().toIntOrNull() ?: 0

    private inline val weightDouble: Float
        get() = weights.text.toString().toFloatOrNull() ?: 0F

    @AssistedFactory
    interface AddSetViewModelFactory {
        fun create(id: Int): AddSetViewModel
    }

    object IntTransformation : InputTransformation {
        override val keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        override fun TextFieldBuffer.transformInput() {
            if (!asCharSequence().isDigitsOnly()) {
                revertAllChanges()
            }
        }
    }

    object FloatTransformation : InputTransformation {
        override val keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        override fun TextFieldBuffer.transformInput() {
            toString().toFloatOrNull() ?: revertAllChanges()
        }
    }
}
