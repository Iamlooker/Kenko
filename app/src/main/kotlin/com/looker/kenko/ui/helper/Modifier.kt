package com.looker.kenko.ui.helper

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.layout

fun Modifier.vertical(towardsRight: Boolean = true) =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }.rotate(90F * (if (towardsRight) 1 else -1))

const val PHI = 16F / 10F
