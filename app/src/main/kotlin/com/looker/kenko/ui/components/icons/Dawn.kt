package com.looker.kenko.ui.components.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Dawn: ImageVector
    get() {
        if (_dawn != null) {
            return _dawn!!
        }
        _dawn = icon(
            name = "Dawn",
            viewPort = 92F to 92F,
            size = 164.dp to 164.dp,
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = SolidColor(Color.Black),
                strokeLineWidth = 3.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(46.0f, 0.0f)
                lineTo(46.0089f, 45.9547f)
                lineTo(63.6035f, 3.5015f)
                lineTo(46.0256f, 45.9618f)
                lineTo(78.5268f, 13.4731f)
                lineTo(46.0382f, 45.9744f)
                lineTo(88.4984f, 28.3965f)
                lineTo(46.0453f, 45.9911f)
                lineTo(92.0f, 46.0f)
                lineTo(46.0453f, 46.0089f)
                lineTo(88.4984f, 63.6035f)
                lineTo(46.0382f, 46.0256f)
                lineTo(78.5268f, 78.5268f)
                lineTo(46.0256f, 46.0382f)
                lineTo(63.6035f, 88.4984f)
                lineTo(46.0089f, 46.0453f)
                lineTo(46.0f, 92.0f)
                lineTo(45.9911f, 46.0453f)
                lineTo(28.3965f, 88.4984f)
                lineTo(45.9744f, 46.0382f)
                lineTo(13.4731f, 78.5268f)
                lineTo(45.9618f, 46.0256f)
                lineTo(3.5015f, 63.6035f)
                lineTo(45.9547f, 46.0089f)
                lineTo(0.0f, 46.0f)
                lineTo(45.9547f, 45.9911f)
                lineTo(3.5015f, 28.3965f)
                lineTo(45.9618f, 45.9744f)
                lineTo(13.4731f, 13.4731f)
                lineTo(45.9744f, 45.9618f)
                lineTo(28.3965f, 3.5015f)
                lineTo(45.9911f, 45.9547f)
                lineTo(46.0f, 0.0f)
                close()
            }
        }
        return _dawn!!
    }

private var _dawn: ImageVector? = null
