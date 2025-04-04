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

package com.looker.kenko

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.looker.kenko.data.local.dao.PerformanceDao
import com.looker.kenko.data.local.dao.RatingWrapper
import com.looker.kenko.data.local.dao.toPerformance
import com.looker.kenko.data.local.model.SetType
import com.looker.kenko.data.local.model.defaultSetTypes
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.PlanItem
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.model.rating
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.data.repository.Performance
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.data.repository.SessionRepo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.properties.Delegates
import kotlin.random.Random
import kotlin.test.assertContentEquals
import kotlin.test.assertNotNull

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PerformanceDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var performanceDao: PerformanceDao

    @Inject
    lateinit var exerciseRepo: ExerciseRepo

    @Inject
    lateinit var sessionRepo: SessionRepo

    @Inject
    lateinit var planRepo: PlanRepo

    lateinit var firstExercise: Exercise
    lateinit var secondExercise: Exercise
    var firstPlanId by Delegates.notNull<Int>()
    var secondPlanId by Delegates.notNull<Int>()

    @Before
    fun setup() = runTest {
        hiltRule.inject()
        performanceDao.upsertSetTypeLookup(defaultSetTypes())
        firstPlanId = planRepo.createPlan("Plan 1")
        secondPlanId = planRepo.createPlan("Plan 2")
        firstExercise = requireNotNull(exerciseRepo.get(1))
        secondExercise = requireNotNull(exerciseRepo.get(2))
        assertNotNull(firstExercise.id)
        assertNotNull(secondExercise.id)
        DayOfWeek.entries.forEach {
            planRepo.addItem(PlanItem(it, firstExercise, firstPlanId))
            planRepo.addItem(PlanItem(it, secondExercise, firstPlanId))
            planRepo.addItem(PlanItem(it, firstExercise, secondPlanId))
            planRepo.addItem(PlanItem(it, secondExercise, secondPlanId))
        }
    }

    @Test
    fun performanceWithSinglePlan() = runTest {
        planRepo.setCurrent(firstPlanId)
        val sets = fakeSets(firstExercise, localDate)
        sets.forEach { (date, set) ->
            sessionRepo.addSet(sessionRepo.getSessionIdOrCreate(date), set)
        }
        val expectedPerformance = performanceOf(sets)
        val performance = performanceDao.getPerformance(firstExercise.id, firstPlanId)
        assertNotNull(performance)
        assertContentEquals(expectedPerformance.days, performance.days)
        assertContentEquals(expectedPerformance.ratings, performance.ratings)
    }

    @Test
    fun performanceWithMultiplePlans() = runTest {
        val firstPlanSets = fakeSets(firstExercise, localDate)
        planRepo.setCurrent(firstPlanId)
        firstPlanSets.forEach { (date, set) ->
            sessionRepo.addSet(sessionRepo.getSessionIdOrCreate(date), set)
        }

        val expectedFirstPlanPerf = performanceOf(firstPlanSets)
        val firstPlanPerf = performanceDao.getPerformance(firstExercise.id, firstPlanId)
        assertNotNull(firstPlanPerf)
        assertContentEquals(expectedFirstPlanPerf.days, firstPlanPerf.days)
        assertContentEquals(expectedFirstPlanPerf.ratings, firstPlanPerf.ratings)

        val secondPlanSets = fakeSets(firstExercise, localDate + DatePeriod(days = firstPlanSets.size))
        planRepo.setCurrent(secondPlanId)
        secondPlanSets.forEach { (date, set) ->
            sessionRepo.addSet(sessionRepo.getSessionIdOrCreate(date), set)
        }

        val expectedSecondPlanPerf = performanceOf(secondPlanSets)
        val secondPlanPerf = performanceDao.getPerformance(firstExercise.id, secondPlanId)
        assertNotNull(secondPlanPerf)
        assertContentEquals(expectedSecondPlanPerf.days, secondPlanPerf.days)
        assertContentEquals(expectedSecondPlanPerf.ratings, secondPlanPerf.ratings)
    }

    @Test
    fun performanceWithMultipleExercise() = runTest {
        val firstPlanSets = fakeSets(firstExercise, localDate)
        planRepo.setCurrent(firstPlanId)
        firstPlanSets.forEach { (date, set) ->
            sessionRepo.addSet(sessionRepo.getSessionIdOrCreate(date), set)
        }

        val expectedFirstPlanPerf = performanceOf(firstPlanSets)
        val firstPlanPerf = performanceDao.getPerformance(firstExercise.id, firstPlanId)
        assertNotNull(firstPlanPerf)
        assertContentEquals(expectedFirstPlanPerf.days, firstPlanPerf.days)
        assertContentEquals(expectedFirstPlanPerf.ratings, firstPlanPerf.ratings)

        val secondPlanSets = fakeSets(secondExercise, localDate + DatePeriod(days = firstPlanSets.size))
        planRepo.setCurrent(secondPlanId)
        secondPlanSets.forEach { (date, set) ->
            sessionRepo.addSet(sessionRepo.getSessionIdOrCreate(date), set)
        }

        val expectedSecondPlanPerf = performanceOf(secondPlanSets)
        val secondPlanPerf = performanceDao.getPerformance(secondExercise.id, secondPlanId)
        assertNotNull(secondPlanPerf)
        assertContentEquals(expectedSecondPlanPerf.days, secondPlanPerf.days)
        assertContentEquals(expectedSecondPlanPerf.ratings, secondPlanPerf.ratings)
    }

    private fun performanceOf(sets: Map<LocalDate, Set>): Performance = sets
        .map { (date, set) ->
            RatingWrapper(
                date = date.toEpochDays(),
                rating = set.rating.value,
            )
        }
        .sortedBy { it.date }
        .toPerformance()

    private fun fakeSets(exercise: Exercise, startingDate: LocalDate): Map<LocalDate, Set> {
        val baseSet = Set(12, 20F, SetType.Standard, exercise)
        val sets = hashMapOf<LocalDate, Set>()
        repeat(100) {
            val isWeightIncreased = Random.nextBoolean()
            sets[startingDate + DatePeriod(days = it)] = baseSet.copy(
                repsOrDuration = if (!isWeightIncreased) baseSet.repsOrDuration + it else baseSet.repsOrDuration,
                weight = if (isWeightIncreased) baseSet.weight + it else baseSet.weight,
                type = SetType.entries.random(),
            )
        }
        return sets
    }
}
