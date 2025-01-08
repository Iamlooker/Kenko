@file:Suppress("NOTHING_TO_INLINE")

package com.looker.kenko.data.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
@JvmInline
value class PlanStat(private val packedInt: Long) {

    @Stable
    val exercises: Int get() = unpackInt1(packedInt)

    @Stable
    val workDays: Int get() = unpackInt2(packedInt)

    @Stable
    val restDays: Int get() = 7 - workDays
}

fun PlanStat(exercises: Int, workDays: Int): PlanStat {
    return PlanStat(packInts(exercises, workDays))
}

private inline fun packInts(a: Int, b: Int): Long {
    return (a.toLong() shl 32) or (b.toLong() and 0xFFFFFFFF)
}

private inline fun unpackInt1(value: Long): Int {
    return (value shr 32).toInt()
}

private inline fun unpackInt2(value: Long): Int {
    return (value and 0xFFFFFFFF).toInt()
}
