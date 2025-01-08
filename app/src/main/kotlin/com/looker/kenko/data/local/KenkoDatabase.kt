package com.looker.kenko.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.looker.kenko.data.local.dao.ExerciseDao
import com.looker.kenko.data.local.dao.PlanDao
import com.looker.kenko.data.local.dao.PlanHistoryDao
import com.looker.kenko.data.local.dao.SessionDao
import com.looker.kenko.data.local.model.ExerciseEntity
import com.looker.kenko.data.local.model.PlanDayEntity
import com.looker.kenko.data.local.model.PlanEntity
import com.looker.kenko.data.local.model.PlanHistoryEntity
import com.looker.kenko.data.local.model.SessionDataEntity
import com.looker.kenko.data.local.model.SetEntity

@Database(
    version = 2,
    entities = [
        SessionDataEntity::class,
        ExerciseEntity::class,
        PlanEntity::class,
        PlanHistoryEntity::class,
        PlanDayEntity::class,
        SetEntity::class,
    ],
)
@TypeConverters(Converters::class)
abstract class KenkoDatabase : RoomDatabase() {
    abstract val sessionDao: SessionDao
    abstract val exerciseDao: ExerciseDao
    abstract val planDao: PlanDao
    abstract val historyDao: PlanHistoryDao
}

fun kenkoDatabase(context: Context) = Room.databaseBuilder(
    context = context,
    klass = KenkoDatabase::class.java,
    name = "kenko_database",
)
    .createFromAsset("kenko.db")
    .addMigrations(
        MIGRATION_1_2,
    )
    .build()
