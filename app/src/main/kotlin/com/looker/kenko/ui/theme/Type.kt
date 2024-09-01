package com.looker.kenko.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.looker.kenko.R

val FontFamily.Companion.Numbers
    get() = FontFamily(Font(R.font.spacemono_bold))

val displayFont = FontFamily(
    Font(R.font.darkergrotesque_bold, weight = FontWeight.Bold),
    Font(R.font.darkergrotesque_semibold, weight = FontWeight.SemiBold),
)

val bodyFont = FontFamily(
    Font(R.font.spacemono_bold, weight = FontWeight.Bold),
    Font(R.font.spacemono_normal, weight = FontWeight.Normal),
)

fun Typography.header() = displayLarge.copy(
    fontSize = 82.sp,
    lineHeight = 80.0.sp,
)

fun TextStyle.numbers() = copy(fontFamily = FontFamily.Numbers)

val baseline = Typography()

val Typography = Typography().copy(
    displayLarge = baseline.displayLarge.copy(
        fontFamily = displayFont,
        fontWeight = FontWeight.Bold,
    ),
    displayMedium = baseline.displayMedium.copy(
        fontFamily = displayFont,
        fontWeight = FontWeight.Bold,
    ),
    displaySmall = baseline.displaySmall.copy(
        fontFamily = displayFont,
        fontWeight = FontWeight.SemiBold,
    ),
    headlineLarge = baseline.headlineLarge.copy(
        fontFamily = displayFont,
        fontWeight = FontWeight.Bold,
    ),
    headlineMedium = baseline.headlineMedium.copy(
        fontFamily = displayFont,
        fontWeight = FontWeight.Bold,
    ),
    headlineSmall = baseline.headlineSmall.copy(
        fontFamily = displayFont,
        fontWeight = FontWeight.SemiBold,
    ),
    titleLarge = baseline.titleLarge.copy(
        fontFamily = displayFont,
        fontWeight = FontWeight.SemiBold,
    ),
    titleMedium = baseline.titleMedium.copy(
        fontFamily = displayFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
    ),
    titleSmall = baseline.titleSmall.copy(
        fontFamily = displayFont,
        fontWeight = FontWeight.SemiBold,
    ),
    bodyLarge = baseline.bodyLarge.copy(
        fontFamily = bodyFont,
        fontWeight = FontWeight.Normal,
    ),
    bodyMedium = baseline.bodyMedium.copy(
        fontFamily = bodyFont,
        fontWeight = FontWeight.Normal,
    ),
    bodySmall = baseline.bodySmall.copy(
        fontFamily = bodyFont,
        fontWeight = FontWeight.Normal,
    ),
    labelLarge = baseline.labelLarge.copy(
        fontFamily = bodyFont,
        fontWeight = FontWeight.Normal,
    ),
    labelMedium = baseline.labelMedium.copy(
        fontFamily = bodyFont,
        fontWeight = FontWeight.Normal,
    ),
    labelSmall = baseline.labelSmall.copy(
        fontFamily = bodyFont,
        fontWeight = FontWeight.Normal,
    ),
)