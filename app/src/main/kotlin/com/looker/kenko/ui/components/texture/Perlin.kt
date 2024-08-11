package com.looker.kenko.ui.components.texture

import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.random.Random

fun noise2D(x: Double, y: Double): Double {
    return Perlin.noise(x, y, 0.0).absoluteValue
}

/**
 * inspired: https://rosettacode.org/wiki/Perlin_noise#Kotlin
 */
object Perlin {

    private val permutation = randomIntArray(
        size = 512,
        seed = -8937972728836847419,
    )

    fun noise(x: Double, y: Double, z: Double): Double {
        // Find unit cube that contains point
        val xi = floor(x).toInt() and 255
        val yi = floor(y).toInt() and 255
        val zi = floor(z).toInt() and 255

        // Find relative x, y, z of point in cube
        val xx = x - floor(x)
        val yy = y - floor(y)
        val zz = z - floor(z)

        // Compute fade curves for each of xx, yy, zz
        val u = fade(xx)
        val v = fade(yy)
        val w = fade(zz)

        // Hash co-ordinates of the 8 cube corners
        // and add blended results from 8 corners of cube

        val a = permutation[xi] + yi
        val aa = permutation[a] + zi
        val ab = permutation[a + 1] + zi
        val b = permutation[xi + 1] + yi
        val ba = permutation[b] + zi
        val bb = permutation[b + 1] + zi

        return lerp(
            w,
            lerp(
                v,
                lerp(
                    u, grad(permutation[aa], xx, yy, zz),
                    grad(permutation[ba], xx - 1, yy, zz)
                ),
                lerp(
                    u, grad(permutation[ab], xx, yy - 1, zz),
                    grad(permutation[bb], xx - 1, yy - 1, zz)
                )
            ),
            lerp(
                v,
                lerp(
                    u, grad(permutation[aa + 1], xx, yy, zz - 1),
                    grad(permutation[ba + 1], xx - 1, yy, zz - 1)
                ),
                lerp(
                    u, grad(permutation[ab + 1], xx, yy - 1, zz - 1),
                    grad(permutation[bb + 1], xx - 1, yy - 1, zz - 1)
                )
            )
        )
    }

    private fun fade(t: Double) = t * t * t * (t * (t * 6 - 15) + 10)

    private fun lerp(t: Double, a: Double, b: Double) = a + t * (b - a)

    private fun grad(hash: Int, x: Double, y: Double, z: Double): Double {
        // Convert low 4 bits of hash code into 12 gradient directions
        val h = hash and 15
        val u = if (h < 8) x else y
        val v = if (h < 4) y else if (h == 12 || h == 14) x else z
        return (if ((h and 1) == 0) u else -u) +
                (if ((h and 2) == 0) v else -v)
    }
}

private fun randomIntArray(
    size: Int,
    seed: Long,
): IntArray {
    val random = Random(seed)
    return IntArray(size) {
        random.nextInt(0, size / 2)
    }
}