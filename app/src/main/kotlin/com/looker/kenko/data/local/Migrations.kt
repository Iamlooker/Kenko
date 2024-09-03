package com.looker.kenko.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
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
        db.execSQL("DROP TABLE `Exercise`")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS sessions (
            `date` INTEGER NOT NULL,
            `sets` TEXT NOT NULL,
            `planId` INTEGER NOT NULL CONSTRAINT `fk_sessions_plan_table_id` REFERENCES `plan_table` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
            `id` INTEGER PRIMARY KEY AUTOINCREMENT
            )
        """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO `sessions` (`date`,`sets`, `planId`) 
            SELECT Session.`date`, Session.`sets`, plan_table.`id`
            FROM `Session` INNER JOIN `plan_table` ON plan_table.`isActive` = 1 OR plan_table.`isActive` = 0
        """.trimIndent()
        )
        db.execSQL("DROP TABLE `Session`")
    }
}
