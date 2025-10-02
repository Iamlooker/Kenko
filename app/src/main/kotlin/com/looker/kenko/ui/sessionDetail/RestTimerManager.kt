package com.looker.kenko.ui.sessionDetail

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.time.Duration
import java.time.LocalDateTime
import androidx.core.content.edit

class RestTimerManager(private val context: Context) { // <- context qui è già disponibile

    private val KEY_REST_TIME_START = "rest_time_start"

    private val preferences: SharedPreferences =
        context.getSharedPreferences(KEY_REST_TIME_START, Context.MODE_PRIVATE)

    private var timeStampStartSet: LocalDateTime? = loadStartTime()
    private var timeStampNow: LocalDateTime? = null

    fun updateTimer(): Int {
        timeStampNow = LocalDateTime.now()
        timeStampStartSet = loadStartTime()

        preferences.all.forEach { (key, value) ->
            Log.d("Luca - PrefsDebug", "$key -> $value")}

            Log.d("Luca - timeStampNow", timeStampNow.toString())
            Log.d("Luca - timeStampStartSet", timeStampStartSet.toString())
        return if (timeStampStartSet != null) {
            Duration.between(timeStampStartSet, timeStampNow).seconds.toInt()
        } else 0
    }

    fun resetTimer() {
        timeStampStartSet = LocalDateTime.now()
        saveStartTime(timeStampStartSet!!)
    }

    private fun saveStartTime(time: LocalDateTime) {
        preferences.edit { putString(KEY_REST_TIME_START, time.toString()) }
    }

    private fun loadStartTime(): LocalDateTime? {
        return preferences.getString(KEY_REST_TIME_START, null)?.let { LocalDateTime.parse(it) }
    }
}

