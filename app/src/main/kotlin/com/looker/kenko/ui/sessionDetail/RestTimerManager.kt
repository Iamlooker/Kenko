package com.looker.kenko.ui.sessionDetail

import android.content.Context
import android.content.SharedPreferences
import java.time.Duration
import java.time.LocalDateTime
import androidx.core.content.edit

class RestTimerManager(context: Context) {

    @Suppress("PrivatePropertyName")
    private val KEY_REST_TIME_START = "rest_time_start"

    private val preferences: SharedPreferences =
        context.getSharedPreferences(KEY_REST_TIME_START, Context.MODE_PRIVATE)

    private var timeStampStartSet: LocalDateTime? = loadStartTime()

    fun updateTimer(): Int {
        return if (timeStampStartSet != null) {
            Duration.between(timeStampStartSet, LocalDateTime.now()).seconds.toInt()
        } else 0
    }

    fun resetTimer() {
        timeStampStartSet = LocalDateTime.now()
        saveStartTime(timeStampStartSet!!)
    }

    @Suppress("unused")
    fun setTimer(localDateTime : LocalDateTime){ // Only for testing purposes
        timeStampStartSet = localDateTime
        saveStartTime(timeStampStartSet!!)
    }

    private fun saveStartTime(time: LocalDateTime) {
        preferences.edit { putString(KEY_REST_TIME_START, time.toString()) }
    }

    private fun loadStartTime(): LocalDateTime? {
        return preferences.getString(KEY_REST_TIME_START, null)?.let { LocalDateTime.parse(it) }
    }
}

