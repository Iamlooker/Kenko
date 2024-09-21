package com.looker.kenko.data.local

import android.database.Cursor
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.looker.kenko.data.local.model.SetEntity
import com.looker.kenko.data.local.model.toEntity
import com.looker.kenko.data.model.Set
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.migratePlans()
        db.migrateExercises()
        db.migrateSessions()
    }

    private fun SupportSQLiteDatabase.migratePlans() {
        execSQL(
            """
            CREATE TABLE plans (
            `name` TEXT NOT NULL,
            `exercisesPerDay` TEXT NOT NULL,
            `isActive` INTEGER NOT NULL,
            `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)
            """.trimIndent()
        )
        execSQL(
            """
            INSERT INTO `plans` (`name`,`exercisesPerDay`, `isActive`) 
            SELECT `name`,`exercisesPerDay`, `isActive` FROM `plan_table`
            """.trimIndent()
        )

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
            """.trimIndent()
        )
        execSQL(
            """
            INSERT INTO `exercises` (`name`,`target`,`reference`,`isIsometric`) 
            SELECT `name`,`target`,`reference`,`isIsometric` FROM `Exercise`
            """.trimIndent()
        )
        execSQL("DROP TABLE `Exercise`")
    }

    private fun SupportSQLiteDatabase.migrateSessions() {
        execSQL(
            """
            CREATE TABLE IF NOT EXISTS sessions (
            `date` INTEGER NOT NULL,
            `sets` TEXT NOT NULL,
            `planId` INTEGER NOT NULL CONSTRAINT `fk_sessions_plans_id` REFERENCES `plans` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
            `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)
            """.trimIndent()
        )
        execSQL(
            """
            INSERT INTO `sessions` (`date`, `sets`, `planId`) 
            SELECT Session.`date`, Session.`sets`, plans.`id`
            FROM `Session` INNER JOIN `plans` ON plans.`isActive` = 1 OR plans.`isActive` = 0
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
            """.trimIndent()
        )
        val setsFromDB = query("SELECT `id`, `sets` FROM `sessions`").toSetEntity()
        insertSets(setsFromDB)
        execSQL("DROP TABLE `Session`")
        execSQL("ALTER TABLE `sessions` DROP COLUMN sets")
    }

    private fun Cursor.toSetEntity(): List<SetEntity> {
        val sessionSetMap = mutableListOf<SetEntity>()
        if (moveToFirst()) {
            val idIndex = getColumnIndex("id")
            val setsIndex = getColumnIndex("sets")
            do {
                val id = getInt(idIndex)
                val sets = getString(setsIndex)
                val list = Json.decodeFromString(serializer, sets)
                for (i in list.indices) {
                    sessionSetMap.add(i, list[i].toEntity(id, i))
                }
            } while (moveToNext())
        }
        return sessionSetMap
    }

    private fun SupportSQLiteDatabase.insertSets(sets: List<SetEntity>) {
        val insertStatement = compileStatement(
            """
            INSERT INTO `sets` (`reps`, `weight`, `type`, `order`, `sessionId`, `exerciseId`) 
            VALUES (?, ?, ?, ?, ?, ?)
            """.trimIndent()
        )
        for (i in sets.indices) {
            val set = sets[i]
            with(insertStatement) {
                clearBindings()
                bindLong(1, set.repsOrDuration.toLong())
                bindDouble(2, set.weight.toDouble())
                bindString(3, set.type.name)
                bindLong(4, set.order.toLong())
                bindLong(5, set.sessionId.toLong())
                bindLong(6, set.exerciseId.toLong())
                executeInsert()
            }
        }
    }

    private val serializer = ListSerializer(Set.serializer())
}
