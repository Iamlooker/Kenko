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

package com.looker.kenko.data.progression

import kotlin.test.Test
import kotlin.test.assertEquals

class SessionProgressionTest {

    @Test
    fun when_avgRir_high_progress_weight() {
        val lastWeek = listOf(
            SetPerformance(weight = 100f, reps = 8, rir = 3),
            SetPerformance(weight = 100f, reps = 8, rir = 3),
            SetPerformance(weight = 100f, reps = 8, rir = 3),
        )
        val decision = SessionProgression().suggestNext(lastWeek)
        assertEquals(Decision.Progress(ProgressTarget.Weight, 2.5f), decision)
    }

    @Test
    fun when_avgRir_medium_continue() {
        val lastWeek = listOf(
            SetPerformance(weight = 100f, reps = 8, rir = 2),
            SetPerformance(weight = 100f, reps = 8, rir = 1),
            SetPerformance(weight = 100f, reps = 8, rir = 2),
        )
        val decision = SessionProgression().suggestNext(lastWeek)
        assertEquals(Decision.Continue, decision)
    }

    @Test
    fun when_effort_very_high_deload() {
        val lastWeek = listOf(
            SetPerformance(weight = 100f, reps = 8, rir = 0),
            SetPerformance(weight = 100f, reps = 8, rir = 0),
            SetPerformance(weight = 100f, reps = 7, rir = -1),
        )
        val decision = SessionProgression().suggestNext(lastWeek)
        assertEquals(Decision.Deload(scope = DeloadScope.Exercise), decision)
    }
}
