package com.looker.kenko.data.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.looker.kenko.R
import kotlinx.serialization.Serializable

@Stable
@Serializable
enum class MuscleGroups(@StringRes val stringRes: Int) {
    // Arms
    Biceps(R.string.label_muscle_biceps),
    Triceps(R.string.label_muscle_triceps),
    Shoulders(R.string.label_muscle_delts),

    // Legs
    Quads(R.string.label_muscle_quads),
    Hamstrings(R.string.label_muscle_hamstrings),
    Calves(R.string.label_muscle_calves),
    Glutes(R.string.label_muscle_glutes),

    // Front
    Core(R.string.label_muscle_core),
    Chest(R.string.label_muscle_chest),

    // Back
    Traps(R.string.label_muscle_traps),
    Lats(R.string.label_muscle_lats),
    UpperBack(R.string.label_muscle_back),
}
