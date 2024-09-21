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

    @Query("DELETE FROM exercises WHERE id = :id ")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM exercises")
    fun stream(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun get(id: Int): ExerciseEntity?

    @Query("SELECT EXISTS(SELECT * FROM exercises WHERE name = :name)")
    suspend fun exists(name: String): Boolean

}