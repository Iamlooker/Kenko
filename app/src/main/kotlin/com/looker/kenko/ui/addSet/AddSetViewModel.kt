/*
 * Copyright (C) 2025. LooKeR & Contributors
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
import com.looker.kenko.data.local.model.SetType
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.ui.addSet.components.BoundReached
import com.looker.kenko.ui.addSet.components.Direction
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AddSetViewModel.AddSetViewModelFactory::class)
class AddSetViewModel @AssistedInject constructor(
    private val sessionRepo: SessionRepo,
    @Assisted private val id: Int,
) : ViewModel() {

    val reps: TextFieldState = TextFieldState("12")
    val weights: TextFieldState = TextFieldState("20.0")

    fun addRep(value: Int) {
        reps.setTextAndPlaceCursorAtEnd((repInt + value).toString())
    }

    fun addWeight(value: Float) {
        weights.setTextAndPlaceCursorAtEnd((weightFloat + value).toString())
    }

    val repsBoundReached = BoundReached { direction ->
        when (direction) {
            Direction.Left -> addRep(-1)
            Direction.Right -> addRep(1)
        }
    }

    val weightsBoundReached = BoundReached { direction ->
        when (direction) {
            Direction.Left -> addWeight(-1F)
            Direction.Right -> addWeight(1F)
        }
    }

    fun addSet() {
        viewModelScope.launch {
            val sessionId = sessionRepo.getSessionIdOrCreate(localDate)
            sessionRepo.addSet(
                sessionId = sessionId,
                exerciseId = id,
                weight = weightFloat,
                reps = repInt,
                setType = SetType.Standard,
            )
        }
    }

    private inline val repInt: Int
        get() = reps.text.toString().toIntOrNull() ?: 0

    private inline val weightFloat: Float
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
