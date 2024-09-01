package com.looker.kenko.data.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey
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
import kotlinx.serialization.Serializable

@Entity("exercises")
@Stable
@Serializable
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
    val target: MuscleGroups,
    val reference: String? = null,
    val isIsometric: Boolean = false,
)

@Stable
val Exercise.repDurationStringRes: Int
    @StringRes
    get() = if (isIsometric) R.string.label_duration else R.string.label_reps

private fun MuscleGroups.exercise(block: MutableList<String>.() -> Unit): List<Exercise> {
    val list = mutableListOf<String>()
    list.apply(block)
    return list.map { Exercise(name = it, target = this) }
}

val MuscleGroups.sampleExercises: List<Exercise>
    get() = when (this) {
        Biceps -> exercise { add("Curls"); add("Barbell Curls"); add("Preacher Curls"); add("B-t-B Curls") }
        Triceps -> exercise { add("Push-down"); add("Skull-Crushers"); add("Push-overs") }
        Shoulders -> exercise { add("Lateral Raises"); add("Shoulder Press"); add("Face Pulls") }
        Quads -> exercise { add("Squats"); add("Leg Press"); add("Hack Squats"); add("Leg Extensions") }
        Hamstrings -> exercise { add("SDL"); add("Lying Leg Curls") }
        Calves -> exercise { add("Calve Raises") }
        Glutes -> exercise { add("Hip Thrusts"); add("Lunges") }
        Core -> exercise { add("Sit-ups"); add("Leg Raises") }
        Chest -> exercise { add("Bench Press"); add("Incline Bench"); add("Pec Dec"); add("Chest Fly") }
        Traps -> exercise { add("Shrugs") }
        Lats -> exercise { add("Lat Pull-down"); add("Pull-ups"); add("Lat Prayers") }
        UpperBack -> exercise { add("Bent-over Rows"); add("Chest-Supported Rows"); add("Rows") }
    }
