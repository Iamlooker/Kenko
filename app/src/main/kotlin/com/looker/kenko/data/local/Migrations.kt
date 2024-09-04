package com.looker.kenko.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE plans (
            `name` VARCHAR(100) NOT NULL,
            `exercisesPerDay` TEXT NOT NULL,
            `isActive` INTEGER NOT NULL,
            `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)
            """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO `plans` (`name`,`exercisesPerDay`, `isActive`) 
            SELECT `name`,`exercisesPerDay`, `isActive` FROM `plan_table`
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE exercises (
            `name` VARCHAR(100) NOT NULL,
            `target` VARCHAR(15) NOT NULL,
            `reference` TEXT,
            `isIsometric` INTEGER NOT NULL,
            `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)
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
            `planId` INTEGER NOT NULL CONSTRAINT `fk_sessions_plans_id` REFERENCES `plans` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
            `sets` TEXT NOT NULL,
            `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)
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
        db.execSQL("DROP TABLE `plan_table`")
    }
}
