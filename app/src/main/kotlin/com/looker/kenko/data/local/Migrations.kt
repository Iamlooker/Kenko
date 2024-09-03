package com.looker.kenko.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `plan_table` RENAME TO `plans`")
        db.execSQL(
            """
            CREATE TABLE exercises (
            `name` TEXT NOT NULL,
            `target` TEXT NOT NULL,
            `reference` TEXT,
            `isIsometric` INTEGER NOT NULL,
            `id` INTEGER PRIMARY KEY AUTOINCREMENT
            )
        """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO `exercises` (`name`,`target`,`reference`,`isIsometric`) 
            SELECT `name`,`target`,`reference`,`isIsometric` FROM `Exercise`
        """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS sessions (
            `date` INTEGER NOT NULL,
            `sets` TEXT NOT NULL,
            `planId` INTEGER NOT NULL CONSTRAINT `fk_sessions_plans_id` REFERENCES `plans` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
            `id` INTEGER PRIMARY KEY AUTOINCREMENT
            )
        """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO `sessions` (`date`,`sets`, `planId`) 
            SELECT Session.`date`, Session.`sets`, plans.`id`
            FROM `Session` INNER JOIN `plans` ON plans.`isActive` = 1 OR plans.`isActive` = 0
        """.trimIndent()
        )
        db.execSQL("DROP TABLE `Session`")
        db.execSQL("DROP TABLE `Exercise`")
    }
}
