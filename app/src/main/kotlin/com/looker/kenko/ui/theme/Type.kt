package com.looker.kenko.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.looker.kenko.R

val FontFamily.Companion.Numbers
    get() = FontFamily(Font(R.font.space_mono_bold))

val FontFamily.Companion.Oswald
    get() = FontFamily(
        Font(R.font.oswald_bold, weight = FontWeight.Bold),
        Font(R.font.oswald_semibold, weight = FontWeight.SemiBold),
        Font(R.font.oswald_medium, weight = FontWeight.Medium),
        Font(R.font.oswald_normal, weight = FontWeight.Normal),
        Font(R.font.oswald_light, weight = FontWeight.Light),
    )

val FontFamily.Companion.Poppins
    get() = FontFamily(
        Font(R.font.poppins_regular, weight = FontWeight.Normal),
        Font(R.font.poppins_medium, weight = FontWeight.Medium),
        Font(R.font.poppins_semibold, weight = FontWeight.SemiBold),
    )

fun TextStyle.numbers() = copy(fontFamily = FontFamily.Numbers)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Oswald,
        fontWeight = FontWeight.Normal,
        fontSize = 72.sp,
        lineHeight = 75.0.sp,
        letterSpacing = (-1.4).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Oswald,
        fontWeight = FontWeight.Medium,
        fontSize = 45.sp,
        lineHeight = 52.0.sp,
        letterSpacing = 0.0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Oswald,
        fontWeight = FontWeight.Medium,
        fontSize = 36.sp,
        lineHeight = 44.0.sp,
        letterSpacing = 0.0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Oswald,
        fontWeight = FontWeight.Normal,
        fontSize = 38.sp,
        lineHeight = 36.0.sp,
        letterSpacing = 0.0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Oswald,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.0.sp,
        letterSpacing = 0.0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Oswald,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Oswald,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.2.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Oswald,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.2.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)