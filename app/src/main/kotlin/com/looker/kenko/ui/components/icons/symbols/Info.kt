package com.looker.kenko.ui.components.icons.symbols

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.components.icons.icon
import kotlin.Suppress

val Info: ImageVector
    get() {
        if (_info != null) {
            return _info!!
        }
        _info = icon(
            name = "Info",
            viewPort = 960.0F to 960.0F,
            size = 24.dp to 24.dp,
        ) {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(450f, 670f)
                horizontalLineToRelative(60f)
                verticalLineToRelative(-230f)
                horizontalLineToRelative(-60f)
                verticalLineToRelative(230f)
                close()
                moveTo(480f, 371.54f)
                quadToRelative(13.73f, 0f, 23.02f, -9.29f)
                reflectiveQuadToRelative(9.29f, -23.02f)
                quadToRelative(0f, -13.73f, -9.29f, -23.02f)
                quadToRelative(-9.29f, -9.28f, -23.02f, -9.28f)
                reflectiveQuadToRelative(-23.02f, 9.28f)
                quadToRelative(-9.29f, 9.29f, -9.29f, 23.02f)
                reflectiveQuadToRelative(9.29f, 23.02f)
                quadToRelative(9.29f, 9.29f, 23.02f, 9.29f)
                close()
                moveTo(480.07f, 860f)
                quadToRelative(-78.84f, 0f, -148.21f, -29.92f)
                reflectiveQuadToRelative(-120.68f, -81.21f)
                quadToRelative(-51.31f, -51.29f, -81.25f, -120.63f)
                quadTo(100f, 558.9f, 100f, 480.07f)
                quadToRelative(0f, -78.84f, 29.92f, -148.21f)
                reflectiveQuadToRelative(81.21f, -120.68f)
                quadToRelative(51.29f, -51.31f, 120.63f, -81.25f)
                quadTo(401.1f, 100f, 479.93f, 100f)
                quadToRelative(78.84f, 0f, 148.21f, 29.92f)
                reflectiveQuadToRelative(120.68f, 81.21f)
                quadToRelative(51.31f, 51.29f, 81.25f, 120.63f)
                quadTo(860f, 401.1f, 860f, 479.93f)
                quadToRelative(0f, 78.84f, -29.92f, 148.21f)
                reflectiveQuadToRelative(-81.21f, 120.68f)
                quadToRelative(-51.29f, 51.31f, -120.63f, 81.25f)
                quadTo(558.9f, 860f, 480.07f, 860f)
                close()
                moveTo(480f, 800f)
                quadToRelative(134f, 0f, 227f, -93f)
                reflectiveQuadToRelative(93f, -227f)
                quadToRelative(0f, -134f, -93f, -227f)
                reflectiveQuadToRelative(-227f, -93f)
                quadToRelative(-134f, 0f, -227f, 93f)
                reflectiveQuadToRelative(-93f, 227f)
                quadToRelative(0f, 134f, 93f, 227f)
                reflectiveQuadToRelative(227f, 93f)
                close()
                moveTo(480f, 480f)
                close()
            }
        }

        return _info!!
    }

@Suppress("ObjectPropertyName")
private var _info: ImageVector? = null
