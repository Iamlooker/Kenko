package com.looker.kenko.data.repository

import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.Set
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface SessionRepo {

    val stream: Flow<List<Session>>

    val setsCount: Flow<Int>

    suspend fun addSet(date: LocalDate, set: Set)

    suspend fun removeSet(setId: Int)

    suspend fun createEmpty(date: LocalDate)

    fun getStream(date: LocalDate): Flow<Session?>

    suspend fun getSets(sessionId: Int): List<Set>
}
