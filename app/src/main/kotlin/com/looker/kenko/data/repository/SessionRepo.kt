package com.looker.kenko.data.repository

import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.Set
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface SessionRepo {

    val stream: Flow<List<Session>>

    val exercisesToday: Flow<List<Exercise>?>

    suspend fun updateSet(sets: List<Set>)

    suspend fun createEmpty()

    suspend fun get(date: LocalDate): Session?

    fun getStream(date: LocalDate): Flow<Session?>

}