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

val Arrow2: ImageVector
    get() {
        if (_arrow2 != null) {
            return _arrow2!!
        }
        _arrow2 = icon(
            name = "Arrow2",
            viewPort = 60F to 60F,
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = EvenOdd
            ) {
                moveTo(25.0814f, 0.6872f)
                curveTo(29.776f, 4.8349f, 32.4145f, 10.6669f, 33.6055f, 17.025f)
                curveTo(35.4039f, 26.6272f, 33.9028f, 37.4312f, 31.1225f, 45.5342f)
                curveTo(29.3757f, 50.6271f, 26.4914f, 53.4886f, 23.7002f, 58.0623f)
                curveTo(23.5873f, 58.2476f, 23.6457f, 58.4893f, 23.831f, 58.6023f)
                curveTo(24.0153f, 58.7153f, 24.258f, 58.6568f, 24.37f, 58.4715f)
                curveTo(27.1939f, 53.8443f, 30.098f, 50.9412f, 31.8656f, 45.7889f)
                curveTo(34.6845f, 37.5729f, 36.2005f, 26.6173f, 34.3774f, 16.8804f)
                curveTo(33.1537f, 10.3468f, 30.426f, 4.3612f, 25.6016f, 0.0987f)
                curveTo(25.4391f, -0.045f, 25.1914f, -0.0301f, 25.0477f, 0.1324f)
                curveTo(24.9041f, 0.2949f, 24.9189f, 0.5436f, 25.0814f, 0.6872f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFE5E2DE)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = EvenOdd
            ) {
                moveTo(23.9498f, 59.5777f)
                curveTo(23.9607f, 59.5618f, 24.031f, 59.4548f, 24.0994f, 59.3716f)
                curveTo(24.3808f, 59.0248f, 24.9287f, 58.463f, 25.6619f, 57.7952f)
                curveTo(28.1241f, 55.5579f, 32.6671f, 52.1395f, 36.4115f, 51.7967f)
                curveTo(36.6275f, 51.7769f, 36.786f, 51.5856f, 36.7662f, 51.3696f)
                curveTo(36.7464f, 51.1536f, 36.5561f, 50.9941f, 36.3401f, 51.0139f)
                curveTo(32.4442f, 51.3706f, 27.6951f, 54.8861f, 25.1338f, 57.2145f)
                curveTo(24.791f, 57.5266f, 24.4858f, 57.8169f, 24.2282f, 58.0756f)
                curveTo(24.4502f, 56.9827f, 24.6998f, 55.8977f, 24.9624f, 54.8128f)
                curveTo(25.7194f, 51.6897f, 25.7273f, 48.8311f, 25.0823f, 45.6584f)
                curveTo(25.0397f, 45.4464f, 24.8316f, 45.3087f, 24.6196f, 45.3523f)
                curveTo(24.4075f, 45.3949f, 24.2698f, 45.603f, 24.3134f, 45.815f)
                curveTo(24.9337f, 48.8687f, 24.9277f, 51.6213f, 24.1995f, 54.6275f)
                curveTo(23.8567f, 56.0453f, 23.5356f, 57.4652f, 23.2671f, 58.8989f)
                curveTo(23.2403f, 59.0416f, 23.175f, 59.4132f, 23.174f, 59.4667f)
                curveTo(23.168f, 59.7719f, 23.4157f, 59.8472f, 23.4712f, 59.861f)
                curveTo(23.498f, 59.868f, 23.8349f, 59.9344f, 23.9498f, 59.5777f)
                close()
                moveTo(23.2116f, 59.3101f)
                curveTo(23.2086f, 59.32f, 23.2047f, 59.3299f, 23.2017f, 59.3408f)
                curveTo(23.2037f, 59.3319f, 23.2076f, 59.322f, 23.2116f, 59.3101f)
                close()
            }
        }
        return _arrow2!!
    }

private var _arrow2: ImageVector? = null
