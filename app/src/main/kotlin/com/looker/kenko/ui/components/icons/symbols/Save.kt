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

val Save: ImageVector
    get() {
        if (_save != null) {
            return _save!!
        }
        _save = icon(
            name = "Save",
            viewPort = 960.0F to 960.0F,
            size = 24.dp to 24.dp,
        ) {
            path(
                fill = SolidColor(Color.Black), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(212.31f, 820.0f)
                quadTo(182.0f, 820.0f, 161.0f, 799.0f)
                quadTo(140.0f, 778.0f, 140.0f, 747.69f)
                lineTo(140.0f, 212.31f)
                quadTo(140.0f, 182.0f, 161.0f, 161.0f)
                quadTo(182.0f, 140.0f, 212.31f, 140.0f)
                lineTo(641.61f, 140.0f)
                quadTo(656.08f, 140.0f, 669.42f, 145.62f)
                quadTo(682.77f, 151.23f, 692.61f, 161.08f)
                lineTo(798.92f, 267.39f)
                quadTo(808.77f, 277.23f, 814.38f, 290.58f)
                quadTo(820.0f, 303.92f, 820.0f, 318.39f)
                lineTo(820.0f, 747.69f)
                quadTo(820.0f, 778.0f, 799.0f, 799.0f)
                quadTo(778.0f, 820.0f, 747.69f, 820.0f)
                lineTo(212.31f, 820.0f)
                close()
                moveTo(760.0f, 314.0f)
                lineTo(646.0f, 200.0f)
                lineTo(212.31f, 200.0f)
                quadTo(206.92f, 200.0f, 203.46f, 203.46f)
                quadTo(200.0f, 206.92f, 200.0f, 212.31f)
                lineTo(200.0f, 747.69f)
                quadTo(200.0f, 753.08f, 203.46f, 756.54f)
                quadTo(206.92f, 760.0f, 212.31f, 760.0f)
                lineTo(747.69f, 760.0f)
                quadTo(753.08f, 760.0f, 756.54f, 756.54f)
                quadTo(760.0f, 753.08f, 760.0f, 747.69f)
                lineTo(760.0f, 314.0f)
                close()
                moveTo(480.0f, 690.77f)
                quadTo(521.54f, 690.77f, 550.77f, 661.54f)
                quadTo(580.0f, 632.31f, 580.0f, 590.77f)
                quadTo(580.0f, 549.23f, 550.77f, 520.0f)
                quadTo(521.54f, 490.77f, 480.0f, 490.77f)
                quadTo(438.46f, 490.77f, 409.23f, 520.0f)
                quadTo(380.0f, 549.23f, 380.0f, 590.77f)
                quadTo(380.0f, 632.31f, 409.23f, 661.54f)
                quadTo(438.46f, 690.77f, 480.0f, 690.77f)
                close()
                moveTo(291.54f, 395.38f)
                lineTo(547.69f, 395.38f)
                quadTo(563.15f, 395.38f, 573.5f, 385.04f)
                quadTo(583.84f, 374.69f, 583.84f, 359.23f)
                lineTo(583.84f, 291.54f)
                quadTo(583.84f, 276.08f, 573.5f, 265.73f)
                quadTo(563.15f, 255.39f, 547.69f, 255.39f)
                lineTo(291.54f, 255.39f)
                quadTo(276.08f, 255.39f, 265.73f, 265.73f)
                quadTo(255.39f, 276.08f, 255.39f, 291.54f)
                lineTo(255.39f, 359.23f)
                quadTo(255.39f, 374.69f, 265.73f, 385.04f)
                quadTo(276.08f, 395.38f, 291.54f, 395.38f)
                close()
                moveTo(200.0f, 314.0f)
                lineTo(200.0f, 747.69f)
                quadTo(200.0f, 753.08f, 200.0f, 756.54f)
                quadTo(200.0f, 760.0f, 200.0f, 760.0f)
                lineTo(200.0f, 760.0f)
                quadTo(200.0f, 760.0f, 200.0f, 756.54f)
                quadTo(200.0f, 753.08f, 200.0f, 747.69f)
                lineTo(200.0f, 212.31f)
                quadTo(200.0f, 206.92f, 200.0f, 203.46f)
                quadTo(200.0f, 200.0f, 200.0f, 200.0f)
                lineTo(200.0f, 200.0f)
                lineTo(200.0f, 314.0f)
                close()
            }
        }
        return _save!!
    }

private var _save: ImageVector? = null
