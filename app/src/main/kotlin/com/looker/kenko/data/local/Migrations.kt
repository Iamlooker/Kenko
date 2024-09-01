package com.looker.kenko.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE exercises (
            `name` TEXT NOT NULL,
            `target` TEXT NOT NULL,
            `reference` TEXT,
            `isIsometric` INTEGER NOT NULL,
            `id` INTEGER PRIMARY KEY AUTOINCREMENT
            )
        """.trimIndent())
        db.execSQL("""
            INSERT INTO `exercises` (`name`,`target`,`reference`,`isIsometric`) 
            SELECT `name`,`target`,`reference`,`isIsometric` FROM `Exercise`
        """.trimIndent())
        db.execSQL("DROP TABLE `Exercise`")
    }
}