package com.looker.kenko

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.looker.kenko.data.model.PlanItem
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.SetType
import com.looker.kenko.data.model.localDate
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.data.repository.PlanRepo
import com.looker.kenko.data.repository.SessionRepo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DayOfWeek
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var sessionRepo: SessionRepo

    @Inject
    lateinit var planRepo: PlanRepo

    @Inject
    lateinit var exerciseRepo: ExerciseRepo

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun checkPlanDeletion() = runTest {
        val planId = planRepo.createPlan("test")
        val exercises = (1..10).map { exerciseRepo.get(it)!! }
        exercises.forEach {
            planRepo.addItem(
                PlanItem(
                    dayOfWeek = DayOfWeek(Random.nextInt(1, 5)),
                    exercise = it,
                    planId = planId,
                ),
            )
        }
        val planItems = planRepo.getPlanItems(planId)
        assertEquals(10, planItems.size)
        planRepo.setCurrent(planId)
        sessionRepo.createEmpty(localDate)
        val stream = sessionRepo.getStream(localDate)
        assertNotNull(stream.first())
        val sessionId = stream.first()!!.id!!
        val sets = (1..24).map { Set(12, 12F, SetType.entries.random(), exercises.random()) }
        sets.forEach { sessionRepo.addSet(it) }
        assertEquals(24, sessionRepo.getSets(sessionId).size)
        val set = sessionRepo.getSets(sessionId).first()
        sessionRepo.removeSet(set.id!!)
        assertEquals(23, sessionRepo.getSets(sessionId).size)
        val randomPerformedExercise = sets.random().exercise
        val setsForRandomExercise = sets.filter { it.exercise.id == randomPerformedExercise.id }
        exerciseRepo.remove(randomPerformedExercise.id!!)
        assertEquals(23 - setsForRandomExercise.size, sessionRepo.getSets(sessionId).size)
        planRepo.deletePlan(planId)
        assertEquals(23 - setsForRandomExercise.size, sessionRepo.getSets(sessionId).size)
        assertFails {
            planRepo.addItem(
                PlanItem(
                    dayOfWeek = DayOfWeek(Random.nextInt(1, 5)),
                    exercise = randomPerformedExercise,
                    planId = planId,
                ),
            )
        }
        assertEquals(null, stream.first()!!.planId)
        val planItemsAfter = planRepo.getPlanItems(planId)
        assertEquals(0, planItemsAfter.size)
    }
}
