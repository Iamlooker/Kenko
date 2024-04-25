package com.looker.kenko.ui.components.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path

val Cloud: ImageVector
    get() {
        if (_cloud != null) {
            return _cloud!!
        }
        _cloud = icon(
            name = "Cloud",
            size = 118F to 100F
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = EvenOdd
            ) {
                moveTo(69.705f, 23.878f)
                curveTo(67.542f, 22.111f, 65.151f, 20.573f, 62.675f, 19.323f)
                curveTo(57.833f, 16.879f, 50.563f, 14.678f, 43.832f, 14.616f)
                curveTo(39.218f, 14.573f, 34.86f, 15.541f, 31.64f, 18.035f)
                curveTo(25.509f, 22.783f, 24.246f, 30.986f, 25.998f, 39.508f)
                curveTo(27.276f, 45.724f, 30.145f, 52.102f, 33.769f, 57.338f)
                curveTo(29.653f, 54.981f, 24.33f, 52.795f, 19.161f, 52.257f)
                curveTo(13.367f, 51.649f, 7.773f, 53.089f, 4.122f, 58.449f)
                curveTo(-3.509f, 69.652f, 0.312f, 80.491f, 8.602f, 88.315f)
                curveTo(16.746f, 96.0f, 29.235f, 100.717f, 39.092f, 99.911f)
                curveTo(39.613f, 99.866f, 40.001f, 99.408f, 39.958f, 98.885f)
                curveTo(39.916f, 98.364f, 39.458f, 97.974f, 38.938f, 98.019f)
                curveTo(29.546f, 98.784f, 17.66f, 94.257f, 9.901f, 86.933f)
                curveTo(2.287f, 79.747f, -1.322f, 69.807f, 5.686f, 59.521f)
                curveTo(8.902f, 54.801f, 13.861f, 53.608f, 18.965f, 54.143f)
                curveTo(24.275f, 54.699f, 29.749f, 57.101f, 33.766f, 59.537f)
                curveTo(34.854f, 60.195f, 35.952f, 60.847f, 37.041f, 61.514f)
                curveTo(39.961f, 64.795f, 43.162f, 67.266f, 46.306f, 68.462f)
                curveTo(47.087f, 68.756f, 47.446f, 68.244f, 47.54f, 68.095f)
                curveTo(47.668f, 67.893f, 47.792f, 67.567f, 47.585f, 67.146f)
                curveTo(47.546f, 67.067f, 47.451f, 66.918f, 47.28f, 66.754f)
                curveTo(47.048f, 66.532f, 46.53f, 66.124f, 46.315f, 65.925f)
                curveTo(45.428f, 65.096f, 44.455f, 64.333f, 43.498f, 63.593f)
                curveTo(41.834f, 62.302f, 40.067f, 61.154f, 38.274f, 60.049f)
                curveTo(38.017f, 59.755f, 37.763f, 59.458f, 37.512f, 59.154f)
                curveTo(33.015f, 53.703f, 29.328f, 46.303f, 27.853f, 39.126f)
                curveTo(26.259f, 31.37f, 27.219f, 23.858f, 32.799f, 19.537f)
                curveTo(35.703f, 17.288f, 39.652f, 16.476f, 43.815f, 16.514f)
                curveTo(50.248f, 16.574f, 57.195f, 18.682f, 61.823f, 21.019f)
                curveTo(64.65f, 22.446f, 67.362f, 24.266f, 69.719f, 26.388f)
                curveTo(70.501f, 27.092f, 71.215f, 27.892f, 71.899f, 28.692f)
                curveTo(71.974f, 28.78f, 72.077f, 28.91f, 72.193f, 29.059f)
                curveTo(72.491f, 29.506f, 72.807f, 29.936f, 73.14f, 30.349f)
                curveTo(73.315f, 30.566f, 73.435f, 30.644f, 73.457f, 30.658f)
                curveTo(74.03f, 31.025f, 74.436f, 30.717f, 74.609f, 30.557f)
                curveTo(74.661f, 30.51f, 75.18f, 30.0f, 74.758f, 29.33f)
                curveTo(74.672f, 29.195f, 74.137f, 28.468f, 73.711f, 27.921f)
                curveTo(70.552f, 23.151f, 69.568f, 16.438f, 70.964f, 10.989f)
                curveTo(71.849f, 7.534f, 73.695f, 4.582f, 76.621f, 3.042f)
                curveTo(78.567f, 2.018f, 80.98f, 1.628f, 83.876f, 2.094f)
                curveTo(90.859f, 3.216f, 96.708f, 7.21f, 101.914f, 12.126f)
                curveTo(107.239f, 17.156f, 111.889f, 23.156f, 116.346f, 28.176f)
                curveTo(116.693f, 28.568f, 117.292f, 28.603f, 117.683f, 28.254f)
                curveTo(118.073f, 27.906f, 118.108f, 27.306f, 117.761f, 26.914f)
                curveTo(113.27f, 21.855f, 108.579f, 15.814f, 103.213f, 10.745f)
                curveTo(97.727f, 5.563f, 91.535f, 1.402f, 84.176f, 0.219f)
                curveTo(80.807f, -0.322f, 78.004f, 0.17f, 75.74f, 1.361f)
                curveTo(72.353f, 3.145f, 70.153f, 6.517f, 69.129f, 10.517f)
                curveTo(68.059f, 14.696f, 68.281f, 19.553f, 69.705f, 23.878f)
                close()
            }
        }
        return _cloud!!
    }

private var _cloud: ImageVector? = null
