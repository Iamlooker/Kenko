/*
 * Copyright (C) 2025 LooKeR & Contributors
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.looker.kenko

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication
import kotlin.math.pow
import kotlin.math.sqrt

class KenkoTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader,
        appName: String,
        context: Context,
    ): Application {
        return super.newApplication(
            cl,
            HiltTestApplication::class.java.name,
            context,
        )
    }
}

internal inline fun benchmark(
    repetition: Int,
    extraMessage: String? = null,
    block: () -> Long,
): String {
    if (extraMessage != null) {
        println("=".repeat(50))
        println(extraMessage)
        println("=".repeat(50))
    }
    val times = DoubleArray(repetition)
    repeat(repetition) { iteration ->
        System.gc()
        System.runFinalization()
        times[iteration] = block().toDouble()
    }
    val meanAndDeviation = times.culledMeanAndDeviation()
    return buildString {
        append("=".repeat(50))
        append("\n")
        append(times.joinToString(" | "))
        append("\n")
        append("${meanAndDeviation.first} ms ± ${meanAndDeviation.second.toFloat()} ms")
        append("\n")
        append("=".repeat(50))
        append("\n")
    }
}

private fun DoubleArray.culledMeanAndDeviation(): Pair<Double, Double> {
    sort()
    return meanAndDeviation()
}

private fun DoubleArray.meanAndDeviation(): Pair<Double, Double> {
    val mean = average()
    return mean to sqrt(fold(0.0) { acc, value -> acc + (value - mean).pow(2) } / size)
}
