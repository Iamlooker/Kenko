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

package com.looker.kenko.ui.components.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path

val Arrow1: ImageVector
    get() {
        if (_arrow1 != null) {
            return _arrow1!!
        }
        _arrow1 = icon(
            name = "Arrow1",
            viewPort = 57.0f to 57.0f
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = EvenOdd
            ) {
                moveTo(11.4129f, 20.3807f)
                curveTo(11.8677f, 20.1523f, 12.2584f, 19.9533f, 12.4643f, 19.847f)
                curveTo(13.7156f, 19.1974f, 17.0194f, 17.961f, 19.0214f, 16.3208f)
                curveTo(20.5492f, 15.07f, 21.326f, 13.5811f, 20.3871f, 11.9663f)
                curveTo(19.5338f, 10.4987f, 17.6564f, 9.9976f, 15.3326f, 10.1916f)
                curveTo(10.3313f, 10.6092f, 3.217f, 14.1335f, 1.3427f, 15.7533f)
                curveTo(1.1381f, 15.9302f, 1.1153f, 16.2385f, 1.2922f, 16.443f)
                curveTo(1.4683f, 16.6467f, 1.7766f, 16.6695f, 1.9811f, 16.4925f)
                curveTo(3.7802f, 14.9386f, 10.6128f, 11.5664f, 15.4141f, 11.1654f)
                curveTo(17.2908f, 11.0089f, 18.8532f, 11.2725f, 19.5423f, 12.4582f)
                curveTo(20.223f, 13.6281f, 19.51f, 14.6577f, 18.4024f, 15.5645f)
                curveTo(16.4544f, 17.1611f, 13.2325f, 18.3475f, 12.0138f, 18.9794f)
                curveTo(11.25f, 19.3759f, 8.2942f, 20.8058f, 8.0703f, 20.9894f)
                curveTo(7.7899f, 21.2191f, 7.882f, 21.4884f, 7.9189f, 21.5745f)
                curveTo(7.9474f, 21.6413f, 8.1109f, 21.965f, 8.5402f, 21.8504f)
                curveTo(14.6622f, 20.2202f, 25.185f, 21.9561f, 33.3426f, 26.7524f)
                curveTo(41.4286f, 31.5065f, 47.1978f, 39.3f, 43.8692f, 49.8987f)
                curveTo(43.7888f, 50.1563f, 43.9323f, 50.4309f, 44.189f, 50.5121f)
                curveTo(44.4466f, 50.5925f, 44.7211f, 50.449f, 44.8015f, 50.1914f)
                curveTo(48.2889f, 39.0862f, 42.3096f, 30.8905f, 33.8386f, 25.91f)
                curveTo(26.6995f, 21.7122f, 17.7953f, 19.8224f, 11.4129f, 20.3807f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFE5E2DE)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = EvenOdd
            ) {
                moveTo(44.4335f, 49.4713f)
                curveTo(44.0066f, 48.4008f, 43.6156f, 47.3214f, 43.1053f, 46.2836f)
                curveTo(41.7461f, 43.5182f, 39.7482f, 40.9328f, 37.2431f, 39.1197f)
                curveTo(37.0235f, 38.9615f, 36.7183f, 39.0111f, 36.5602f, 39.229f)
                curveTo(36.402f, 39.4486f, 36.4516f, 39.7538f, 36.6695f, 39.912f)
                curveTo(39.0479f, 41.6312f, 40.9382f, 44.091f, 42.2275f, 46.7147f)
                curveTo(42.811f, 47.9029f, 43.2367f, 49.1478f, 43.742f, 50.3687f)
                curveTo(43.768f, 50.4293f, 43.9714f, 50.9665f, 44.0455f, 51.0681f)
                curveTo(44.2166f, 51.3057f, 44.4392f, 51.2948f, 44.5415f, 51.2765f)
                curveTo(44.6272f, 51.2623f, 44.7264f, 51.2248f, 44.823f, 51.1366f)
                curveTo(44.9019f, 51.0639f, 45.0192f, 50.8907f, 45.1267f, 50.6257f)
                curveTo(45.4737f, 49.7781f, 45.9994f, 47.6848f, 46.1221f, 47.3504f)
                curveTo(47.5276f, 43.5163f, 49.4788f, 40.1852f, 51.4454f, 36.6269f)
                curveTo(51.5751f, 36.3902f, 51.4896f, 36.0922f, 51.2539f, 35.9617f)
                curveTo(51.0181f, 35.8311f, 50.7209f, 35.9175f, 50.5904f, 36.1533f)
                curveTo(48.5986f, 39.7565f, 46.6274f, 43.1325f, 45.2054f, 47.0134f)
                curveTo(45.1203f, 47.243f, 44.7192f, 48.5406f, 44.4335f, 49.4713f)
                close()
            }
        }
        return _arrow1!!
    }

private var _arrow1: ImageVector? = null
