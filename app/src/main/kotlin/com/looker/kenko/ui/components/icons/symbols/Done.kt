package com.looker.kenko.ui.components.icons.symbols

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.components.icons.icon

val Done: ImageVector
    get() {
        if (_done != null) {
            return _done!!
        }
        _done = icon(
            name = "Done",
            viewPort = 960.0F to 960.0F,
            size = 24.dp to 24.dp,
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(382.0f, 620.62f)
                lineTo(727.54f, 275.08f)
                quadTo(736.46f, 266.15f, 748.42f, 265.96f)
                quadTo(760.38f, 265.77f, 769.69f, 275.08f)
                quadTo(779.0f, 284.39f, 779.0f, 296.46f)
                quadTo(779.0f, 308.54f, 769.69f, 317.85f)
                lineTo(407.31f, 680.85f)
                quadTo(396.46f, 691.69f, 382.0f, 691.69f)
                quadTo(367.54f, 691.69f, 356.69f, 680.85f)
                lineTo(189.69f, 513.85f)
                quadTo(180.77f, 504.92f, 180.89f, 492.65f)
                quadTo(181.0f, 480.39f, 190.31f, 471.08f)
                quadTo(199.62f, 461.77f, 211.69f, 461.77f)
                quadTo(223.77f, 461.77f, 233.08f, 471.08f)
                lineTo(382.0f, 620.62f)
                close()
            }
        }
        return _done!!
    }

private var _done: ImageVector? = null
