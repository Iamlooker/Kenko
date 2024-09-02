package com.looker.kenko.data.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.looker.kenko.data.local.dao.ExerciseDao
import com.looker.kenko.data.local.dao.PlanDao
import com.looker.kenko.data.local.dao.SessionDao
import com.looker.kenko.data.local.model.ExerciseEntity
import com.looker.kenko.data.local.model.SessionEntity
import com.looker.kenko.data.model.Plan

@Database(
    version = 4,
    entities = [
        SessionEntity::class,
        ExerciseEntity::class,
        Plan::class,
    ],
    autoMigrations = [
        AutoMigration(2,3, MIGRATION_2_3::class)
    ]
)
@TypeConverters(Converters::class)
abstract class KenkoDatabase : RoomDatabase() {
    abstract val sessionDao: SessionDao
    abstract val exerciseDao: ExerciseDao
    abstract val planDao: PlanDao

    companion object {
        fun create(context: Context): KenkoDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = KenkoDatabase::class.java,
                name = "kenko_database",
            )
                .createFromAsset("kenko.db")
                .addMigrations(
                    MIGRATION_1_2,
                    MIGRATION_3_4,
                )
                .build()
        }
    }
}
