/*
 * Copyright (C) 2025 LooKeR & Contributors
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

package com.looker.kenko.data.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.looker.kenko.R
import com.looker.kenko.data.model.MuscleGroups.Biceps
import com.looker.kenko.data.model.MuscleGroups.Calves
import com.looker.kenko.data.model.MuscleGroups.Chest
import com.looker.kenko.data.model.MuscleGroups.Core
import com.looker.kenko.data.model.MuscleGroups.Glutes
import com.looker.kenko.data.model.MuscleGroups.Hamstrings
import com.looker.kenko.data.model.MuscleGroups.Lats
import com.looker.kenko.data.model.MuscleGroups.Quads
import com.looker.kenko.data.model.MuscleGroups.Shoulders
import com.looker.kenko.data.model.MuscleGroups.Traps
import com.looker.kenko.data.model.MuscleGroups.Triceps
import com.looker.kenko.data.model.MuscleGroups.UpperBack
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("exercise")
data class Exercise(
    val name: String,
    val target: MuscleGroups,
    val reference: String? = null,
    val isIsometric: Boolean = false,
    val id: Int? = null,
)

@Stable
val Exercise.repDurationStringRes: Int
    @StringRes
    get() = if (isIsometric) R.string.label_duration else R.string.label_reps

class ExercisesPreviewParameter : PreviewParameterProvider<List<Exercise>> {
    override val values = sequenceOf(
        listOf(
            Exercise("Curls", Biceps),
            Exercise("Barbell Curls", Biceps),
            Exercise("Preacher Curls", Biceps),
            Exercise("B-t-B Curls", Biceps),
        ),
        listOf(
            Exercise("Push-down", Triceps),
            Exercise("Skull-Crushers", Triceps),
            Exercise("Push-overs", Triceps),
        ),
        listOf(
            Exercise("Lateral Raises", Shoulders),
            Exercise("Shoulder Press", Shoulders),
            Exercise("Face Pulls", Shoulders),
        ),
        listOf(
            Exercise("Squats", Quads),
            Exercise("Leg Press", Quads),
            Exercise("Hack Squats", Quads),
            Exercise("Leg Extensions", Quads),
        ),
        listOf(
            Exercise("SDL", Hamstrings),
            Exercise("Lying Leg Curls", Hamstrings),
        ),
        listOf(Exercise("Calve Raises", Calves)),
        listOf(
            Exercise("Hip Thrusts", Glutes),
            Exercise("Lunges", Glutes),
        ),
        listOf(
            Exercise("Sit-ups", Core),
            Exercise("Leg Raises", Core),
        ),
        listOf(
            Exercise("Bench Press", Chest),
            Exercise("Incline Bench", Chest),
            Exercise("Pec Dec", Chest),
            Exercise("Chest Fly", Chest),
        ),
        listOf(Exercise("Shrugs", Traps)),
        listOf(
            Exercise("Lat Pull-down", Lats),
            Exercise("Pull-ups", Lats),
            Exercise("Lat Prayers", Lats),
        ),
        listOf(
            Exercise("Bent-over Rows", UpperBack),
            Exercise("Chest-Supported Rows", UpperBack),
            Exercise("Rows", UpperBack),
        ),
    )
}
