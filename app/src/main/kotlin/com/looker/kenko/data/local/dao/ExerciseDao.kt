package com.looker.kenko.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.looker.kenko.data.local.model.ExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Upsert
    suspend fun upsert(exercise: ExerciseEntity)

    @Query("DELETE FROM exercises WHERE name = :name ")
    suspend fun delete(name: String)

    @Query("SELECT * FROM exercises")
    fun stream(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE name = :name")
    suspend fun get(name: String): ExerciseEntity?

}