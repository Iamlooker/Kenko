package com.looker.kenko.data.model

@JvmInline
value class Rating(val value: Double)

operator fun Rating.plus(other: Rating) = Rating(value + other.value)
