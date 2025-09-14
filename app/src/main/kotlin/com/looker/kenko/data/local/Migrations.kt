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

package com.looker.kenko.data.local

import android.database.Cursor
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteStatement
import com.looker.kenko.data.local.model.ExerciseEntity
import com.looker.kenko.data.model.Set
import com.looker.kenko.data.model.localDate
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.serializers.DayOfWeekSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.json.Json

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.migrateExercises()
        db.migratePlans()
        db.migrateSessions()
    }

    private fun SupportSQLiteDatabase.migratePlans() {
        execSQL(
            """
            CREATE TABLE plans (
            `name` TEXT NOT NULL,
            `description` TEXT DEFAULT NULL,
            `difficulty` TEXT DEFAULT NULL,
            `focus` TEXT DEFAULT NULL,
            `equipment` TEXT DEFAULT NULL,
            `time` TEXT DEFAULT NULL,
            `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)
            """.trimIndent(),
        )
        execSQL(
            """
            CREATE TABLE plan_day (
            `planId` INTEGER NOT NULL CONSTRAINT `fk_sessions_plans_id` REFERENCES `plans` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
            `exerciseId` INTEGER NOT NULL CONSTRAINT `fk_sets_exercises_id` REFERENCES `exercises` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
            `dayOfWeek` INTEGER NOT NULL,
            `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)
            """.trimIndent(),
        )
        createIndex("plan_day", "planId", "exerciseId")
        execSQL(
            """
            CREATE TABLE plan_history (
            `planId` INTEGER CONSTRAINT `fk_sessions_plans_id` REFERENCES `plans` (`id`) ON UPDATE NO ACTION ON DELETE SET NULL,
            `start` INTEGER NOT NULL,
            `end` INTEGER DEFAULT NULL,
            `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)
            """.trimIndent(),
        )
        createIndex("plan_history", "planId", "start", "end")
        execSQL(
            """
            INSERT INTO `plans` (`name`)
            SELECT `name` FROM `plan_table`
            """.trimIndent(),
        )
        val planHistorySelectStatement = query("SELECT `id`, `isActive` FROM `plan_table`")
        val planHistoryInsertStatement = compileStatement(
            """
            INSERT INTO `plan_history` (`planId`, `start`)
            VALUES (?, ?)
            """.trimIndent(),
        )
        val selectStatement = query("SELECT `id`, `exercisesPerDay` FROM `plan_table`")
        val insertStatement = compileStatement(
            """
            INSERT INTO `plan_day` (`dayOfWeek`, `planId`, `exerciseId`)
            VALUES (?, ?, ?)
            """.trimIndent(),
        )
        planHistorySelectStatement.toPlanHistory(planHistoryInsertStatement)
        selectStatement.toPlanDay(this, insertStatement)
        execSQL("DROP TABLE `plan_table`")
    }

    private fun SupportSQLiteDatabase.migrateExercises() {
        execSQL(
            """
            CREATE TABLE exercises (
            `name` TEXT NOT NULL,
            `target` TEXT NOT NULL,
            `reference` TEXT,
            `isIsometric` INTEGER NOT NULL,
            `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)
            """.trimIndent(),
        )
        execSQL(
            """
            INSERT INTO `exercises` (`name`,`target`,`reference`,`isIsometric`)
            SELECT `name`,`target`,`reference`,`isIsometric` FROM `Exercise`
            """.trimIndent(),
        )
        execSQL("DROP TABLE `Exercise`")
    }

    private fun SupportSQLiteDatabase.migrateSessions() {
        /**
         * This `id` can be treated as session id because previous session entity
         * didn't have any id and creating new id per session is like
         * creating new session id
         */
        execSQL(
            """
            CREATE TABLE IF NOT EXISTS _tmp_session (
            `sets` TEXT NOT NULL,
            `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)
            """.trimIndent(),
        )
        execSQL(
            """
            INSERT INTO `_tmp_session` (`sets`)
            SELECT (`sets`)
            FROM `Session`
            """.trimIndent(),
        )
        execSQL(
            """
            CREATE TABLE IF NOT EXISTS sessions (
            `date` INTEGER NOT NULL,
            `planId` INTEGER CONSTRAINT `fk_sessions_plans_id` REFERENCES `plans` (`id`) ON UPDATE NO ACTION ON DELETE SET NULL,
            `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)
            """.trimIndent(),
        )
        createIndex("sessions", "planId")
        execSQL(
            """
            INSERT INTO `sessions` (`date`, `planId`)
            SELECT Session.`date`, plan_history.`planId`
            FROM `Session`
            INNER JOIN `plan_history`
            ON (plan_history.`end` IS NULL
            AND plan_history.`start` IS NOT NULL)
            """.trimIndent(),
        )
        execSQL(
            """
            CREATE TABLE IF NOT EXISTS set_type (
            `type` TEXT NOT NULL PRIMARY KEY,
            `modifier` REAL NOT NULL)
            """.trimIndent()
        )
        execSQL(
            """
            CREATE TABLE IF NOT EXISTS sets (
            `reps` INTEGER NOT NULL,
            `exerciseId` INTEGER NOT NULL CONSTRAINT `fk_sets_exercises_id` REFERENCES `exercises` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
            `weight` REAL NOT NULL,
            `sessionId` INTEGER NOT NULL CONSTRAINT `fk_sets_sessions_id` REFERENCES `sessions` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
            `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
            `type` TEXT NOT NULL,
            `order` INTEGER NOT NULL)
            """.trimIndent(),
        )
        createIndex("sets", "sessionId", "exerciseId")
        val selectStatement = query("SELECT `id`, `sets` FROM `_tmp_session`")
        val insertStatement = compileStatement(
            """
            INSERT INTO `sets` (`reps`, `weight`, `type`, `order`, `sessionId`, `exerciseId`)
            VALUES (?, ?, ?, ?, ?, ?)
            """.trimIndent(),
        )
        selectStatement.toSetEntity(this, insertStatement)
        execSQL("DROP TABLE `Session`")
        execSQL("DROP TABLE `_tmp_session`")
    }

    private fun Cursor.toPlanHistory(insert: SupportSQLiteStatement) {
        if (moveToFirst()) {
            val idIndex = getColumnIndex("id")
            val isActiveIndex = getColumnIndex("isActive")
            do {
                val id = getInt(idIndex)
                val isActive = getInt(isActiveIndex) == 1
                if (isActive) insert.insertPlanHistory(id)
            } while (moveToNext())
        }
    }

    private fun Cursor.toPlanDay(db: SupportSQLiteDatabase, insert: SupportSQLiteStatement) {
        if (moveToFirst()) {
            val idIndex = getColumnIndex("id")
            val exerciseMapIndex = getColumnIndex("exercisesPerDay")
            do {
                val id = getInt(idIndex)
                val exerciseMapString = getString(exerciseMapIndex)
                val exerciseMap = Json.decodeFromString(exerciseMapSerializer, exerciseMapString)
                exerciseMap.forEach { (day, exercises) ->
                    exercises.forEach { exercise ->
                        insert.insertPlanDays(day, id, db.exerciseId(exercise.name))
                    }
                }
            } while (moveToNext())
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun SupportSQLiteStatement.insertPlanDays(
        dayOfWeek: DayOfWeek,
        planId: Int,
        exerciseId: Int,
    ) {
        clearBindings()
        bindLong(1, dayOfWeek.isoDayNumber.toLong())
        bindLong(2, planId.toLong())
        bindLong(3, exerciseId.toLong())
        executeInsert()
    }

    private fun SupportSQLiteDatabase.exerciseId(name: String): Int {
        val getId = query("SELECT id FROM exercises WHERE name = ?", arrayOf(name))
        getId.moveToFirst()
        return getId.getInt(getId.getColumnIndexOrThrow("id"))
    }

    private fun Cursor.toSetEntity(db: SupportSQLiteDatabase, insert: SupportSQLiteStatement) {
        if (moveToFirst()) {
            val idIndex = getColumnIndex("id")
            val setsIndex = getColumnIndex("sets")
            do {
                val sessionId = getInt(idIndex)
                val setsString = getString(setsIndex)
                val sets = Json.decodeFromString(setsSerializer, setsString)
                for (i in sets.indices) {
                    insert.insertSet(db, sets[i], sessionId, i)
                }
            } while (moveToNext())
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun SupportSQLiteStatement.insertSet(
        db: SupportSQLiteDatabase,
        set: Set,
        sessionId: Int,
        order: Int
    ) {
        clearBindings()
        bindLong(1, set.repsOrDuration.toLong())
        bindDouble(2, set.weight.toDouble())
        bindString(3, set.type.name)
        bindLong(4, order.toLong())
        bindLong(5, sessionId.toLong())
        val exerciseId = db.exerciseId(set.exercise.name)
        bindLong(6, exerciseId.toLong())
        executeInsert()
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun SupportSQLiteStatement.insertPlanHistory(id: Int) {
        clearBindings()
        bindLong(1, id.toLong())
        bindLong(2, localDate.toEpochDays().toLong())
        executeInsert()
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun SupportSQLiteDatabase.createIndex(
        tableName: String,
        vararg column: String,
    ) {
        val columns = column.joinToString("_")
        val columnInTable = column.joinToString(",") { "`$it`" }
        execSQL(
            """
            CREATE INDEX IF NOT EXISTS `index_${tableName}_$columns`
            ON `$tableName` ($columnInTable)
            """.trimIndent()
        )
    }

    private val exerciseMapSerializer =
        MapSerializer(DayOfWeekSerializer, ListSerializer(ExerciseEntity.serializer()))

    private val setsSerializer = ListSerializer(Set.serializer())
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE sets ADD COLUMN rpe REAL NOT NULL DEFAULT 8.0")
        db.execSQL("ALTER TABLE sets ADD COLUMN rir INTEGER NOT NULL DEFAULT 2")
    }
}
