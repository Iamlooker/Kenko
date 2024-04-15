package com.looker.kenko.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.looker.kenko.data.local.dao.ExerciseDao
import com.looker.kenko.data.local.dao.PlanDao
import com.looker.kenko.data.local.dao.SessionDao
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.model.Session

@Database(
    version = 3,
    entities = [
        Session::class,
        Exercise::class,
        Plan::class,
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
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}