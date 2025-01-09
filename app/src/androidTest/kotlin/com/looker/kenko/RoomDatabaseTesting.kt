package com.looker.kenko

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.looker.kenko.data.local.KenkoDatabase
import com.looker.kenko.data.local.MIGRATION_1_2
import com.looker.kenko.data.local.dao.ExerciseDao
import com.looker.kenko.data.local.dao.PlanDao
import com.looker.kenko.data.local.model.ExerciseEntity
import com.looker.kenko.data.model.MuscleGroups
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.test.assertContains
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RoomDatabaseTesting {

    private val DB_NAME = "test.db"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        KenkoDatabase::class.java,
    )

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var exerciseDao: ExerciseDao

    @Inject
    lateinit var planDao: PlanDao

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun schemaMigration1To2() = runTest {
        val db = helper.createDatabase(DB_NAME, 1)
        db.addV1Data()
        helper.runMigrationsAndValidate(DB_NAME, 2, true, MIGRATION_1_2)
    }

    @Test
    fun dataMigration1To2() = runTest {
        val db = helper.createDatabase(DB_NAME, 1)
        db.addV1Data()
        val updatedDb = Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            KenkoDatabase::class.java,
            DB_NAME,
        ).addMigrations(MIGRATION_1_2).build()
        val exercises = updatedDb.exerciseDao.stream().first()
        val planHistory = updatedDb.historyDao.getCurrent()
        assertNotNull(planHistory)
        assertNotNull(planHistory.planId)
        val fullHistory = updatedDb.historyDao.getAll()
        val session = updatedDb.sessionDao.getSession(LocalDate.fromEpochDays(412))
        val emptySession = updatedDb.sessionDao.getSession(LocalDate.fromEpochDays(413))
        val currentPlan = updatedDb.planDao.getPlanById(planHistory.planId)
        val currentPlanItems = updatedDb.planDao.getPlanItemsByPlanId(planHistory.planId)
        assertNotNull(session)
        assertNotNull(emptySession)
        assertNotNull(currentPlan)
        assertEquals(session.sets.size, 6)
        assertContentEquals(
            session.sets.map {
                updatedDb.exerciseDao.get(it.exerciseId)?.name ?: ""
            },
            listOf("Pullups", "Rows", "Press", "Shrugs", "Curls", "Plank"),
        )
        assertEquals(emptySession.sets.size, 0)
        assertEquals(exercises.size, 6)
        assertTrue(currentPlanItems.all { it.exerciseId != 0 })
        assertContains(
            exercises,
            ExerciseEntity("Pullups", MuscleGroups.Lats, isIsometric = false, id = 1),
        )
        assertEquals(planHistory.planId, 1)
        assertEquals(fullHistory.size, 1)
        println("Current plan id: ${planHistory.planId}")
        println("Full history: $fullHistory")
        println("Current Plan: $currentPlan")
    }

    @Test
    fun prepopulatedData() = runTest {
        val plans = planDao.plansFlow().first()
        assertTrue(plans.isNotEmpty())
        assertTrue(exerciseDao.stream().first().isNotEmpty())
        val plan = plans.first()
        assertTrue(planDao.getPlanItemsByPlanId(plan.id).isNotEmpty())
    }

    private fun SupportSQLiteDatabase.addV1Data() = use { db ->
        val setJson = arrayOf(
            "{\"repsOrDuration\":62,\"weight\":44.85,\"type\":\"Standard\",\"exercise\":{\"name\":\"Pullups\",\"target\":\"Lats\",\"reference\":null,\"isIsometric\":false}}",
            "{\"repsOrDuration\":12,\"weight\":22.84,\"type\":\"Standard\",\"exercise\":{\"name\":\"Rows\",\"target\":\"UpperBack\",\"reference\":null,\"isIsometric\":false}}",
            "{\"repsOrDuration\":37,\"weight\":68.43,\"type\":\"Standard\",\"exercise\":{\"name\":\"Press\",\"target\":\"Chest\",\"reference\":null,\"isIsometric\":false}}",
            "{\"repsOrDuration\":90,\"weight\":67.92,\"type\":\"Standard\",\"exercise\":{\"name\":\"Shrugs\",\"target\":\"Traps\",\"reference\":null,\"isIsometric\":false}}",
            "{\"repsOrDuration\":86,\"weight\":63.94,\"type\":\"Standard\",\"exercise\":{\"name\":\"Curls\",\"target\":\"Biceps\",\"reference\":null,\"isIsometric\":false}}",
            "{\"repsOrDuration\":86,\"weight\":63.94,\"type\":\"Standard\",\"exercise\":{\"name\":\"Plank\",\"target\":\"Core\",\"reference\":null,\"isIsometric\":true}}",
        ).joinToString(",")
        db.execSQL("""INSERT INTO Exercise (name, target, isIsometric) VALUES('Pullups', 'Lats', 0)""")
        db.execSQL("""INSERT INTO Exercise (name, target, isIsometric) VALUES('Rows', 'UpperBack', 0)""")
        db.execSQL("""INSERT INTO Exercise (name, target, isIsometric) VALUES('Press', 'Chest', 0)""")
        db.execSQL("""INSERT INTO Exercise (name, target, isIsometric) VALUES('Shrugs', 'Traps', 0)""")
        db.execSQL("""INSERT INTO Exercise (name, target, isIsometric) VALUES('Curls', 'Biceps', 0)""")
        db.execSQL("""INSERT INTO Exercise (name, target, isIsometric) VALUES('Plank', 'Core', 1)""")
        val plan =
            "{\"MONDAY\":[{\"name\":\"Pullups\",\"target\":\"Lats\",\"reference\":null,\"isIsometric\":false}],\"TUESDAY\":[{\"name\":\"Rows\",\"target\":\"UpperBack\",\"reference\":null,\"isIsometric\":false}],\"WEDNESDAY\":[{\"name\":\"Press\",\"target\":\"Chest\",\"reference\":null,\"isIsometric\":false}],\"THURSDAY\":[{\"name\":\"Shrugs\",\"target\":\"Traps\",\"reference\":null,\"isIsometric\":false}],\"FRIDAY\":[{\"name\":\"Curls\",\"target\":\"Biceps\",\"reference\":null,\"isIsometric\":false}],\"SUNDAY\":[{\"name\":\"Plank\",\"target\":\"Core\",\"reference\":null,\"isIsometric\":true}]}"
        val inactivePlan =
            "{\"MONDAY\":[{\"name\":\"Pullups\",\"target\":\"Lats\",\"reference\":null,\"isIsometric\":false}],\"TUESDAY\":[{\"name\":\"Rows\",\"target\":\"UpperBack\",\"reference\":null,\"isIsometric\":false}],\"WEDNESDAY\":[{\"name\":\"Press\",\"target\":\"Chest\",\"reference\":null,\"isIsometric\":false}],\"THURSDAY\":[{\"name\":\"Shrugs\",\"target\":\"Traps\",\"reference\":null,\"isIsometric\":false}],\"FRIDAY\":[{\"name\":\"Curls\",\"target\":\"Biceps\",\"reference\":null,\"isIsometric\":false}],\"SUNDAY\":[{\"name\":\"Plank\",\"target\":\"Core\",\"reference\":null,\"isIsometric\":true}]}"
        db.execSQL("""INSERT INTO plan_table (name, exercisesPerDay, isActive) VALUES ('PPL', '$plan', 1)""")
        db.execSQL("""INSERT INTO plan_table (name, exercisesPerDay, isActive) VALUES ('SAME', '$inactivePlan', 0)""")
        db.execSQL("""INSERT INTO Session (date, sets) VALUES (412, '[$setJson]')""")
        db.execSQL("""INSERT INTO Session (date, sets) VALUES (413, '[]')""")
    }
}
