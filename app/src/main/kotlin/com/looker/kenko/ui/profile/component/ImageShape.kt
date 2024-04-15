package com.looker.kenko.ui.profile.component

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import kotlin.math.min

class ProfileImageMorphing(
    private val percentage: Float,
    private val rotation: Float,
) : Shape {

    private val matrix = Matrix()
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        matrix.scale(size.width / 2f, size.height / 2f)
        matrix.translate(1f, 1f)
        matrix.rotateZ(rotation)

        val path = morph.toPath(progress = percentage).asComposePath()
        path.transform(matrix)

        return Outline.Generic(path)
    }

    private companion object {
        const val SX = 0.8F
        const val SY = 0.95F
        val defaultRounding = CornerRounding(0.7f, 0.5F)
        val blob: RoundedPolygon = RoundedPolygon(
            vertices = floatArrayOf(
                -SX, -SY,
                SX, -SY,
                SX, SY,
                -SX, SY,
            ),
            rounding = CornerRounding(min(SX, SY), 0.1F),
            centerX = 0f, centerY = 0f
        )

        val pentagon: RoundedPolygon = RoundedPolygon.star(
            5,
            innerRadius = 0.9F,
            innerRounding = defaultRounding,
            rounding = defaultRounding
        )

        val heptagon: RoundedPolygon = RoundedPolygon.star(
            7,
            innerRadius = 0.9F,
            innerRounding = defaultRounding,
            rounding = defaultRounding
        )

        val morph = Morph(pentagon, heptagon)

    }
}
