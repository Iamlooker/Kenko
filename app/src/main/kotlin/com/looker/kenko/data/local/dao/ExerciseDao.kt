package com.looker.kenko.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.looker.kenko.data.model.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Upsert
    suspend fun upsert(exercise: Exercise)

    @Query("DELETE FROM exercises WHERE name = :name ")
    suspend fun delete(name: String)

    @Query("SELECT * FROM exercises")
    fun stream(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE name = :name")
    suspend fun get(name: String): Exercise?

}