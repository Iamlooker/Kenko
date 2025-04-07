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

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.looker.kenko.data.model.Labels.Difficulty
import com.looker.kenko.data.model.Labels.Equipment
import com.looker.kenko.data.model.Labels.Focus
import com.looker.kenko.data.model.Labels.Time
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Immutable
data class Plan(
    val name: String,
    val description: String?,
    val difficulty: Difficulty?,
    val focus: Focus?,
    val equipment: Equipment?,
    val time: Time?,
    val isActive: Boolean,
    val stat: PlanStat = PlanStat(0, 0),
    val id: Int? = null,
)

@Immutable
data class PlanItem(
    val dayOfWeek: DayOfWeek,
    val exercise: Exercise,
    val planId: Int,
    val id: Long? = null,
)

val localDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

val week = DatePeriod(days = 7)

class PlanPreviewParameters : PreviewParameterProvider<List<Plan>> {
    override val values: Sequence<List<Plan>> = sequenceOf(
        listOf(
            Plan(
                name = "Push Pull Leg",
                description = null,
                difficulty = Difficulty.ADAPTABLE,
                focus = null,
                equipment = Equipment.FULL_GYM,
                time = Time.NORMAL,
                isActive = true,
                stat = PlanStat(21, 5),
            ),
            Plan(
                name = "Upper Lower",
                description = "Alternative upper lower split",
                difficulty = Difficulty.BEGINNER,
                focus = Focus.POWER_BUILDING,
                equipment = Equipment.FULL_GYM,
                time = Time.QUICK,
                isActive = false,
                stat = PlanStat(21, 4),
            ),
            Plan(
                name = "Upper Lower 2",
                description = "Lower Upper split at home",
                difficulty = Difficulty.ADAPTABLE,
                focus = Focus.POWER_BUILDING,
                equipment = Equipment.DUMBBELLS,
                time = Time.QUICK,
                isActive = false,
                stat = PlanStat(21, 5),
            ),
        ),
    )
}
