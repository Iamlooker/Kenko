package com.looker.kenko.data.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class Set(
    val repsOrDuration: Int,
    val weight: Double,
    val exercise: Exercise,
)
