package com.looker.kenko.ui.theme.colorSchemes

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Green & Brown

private val primaryLight = Color(0xFF5D6146)
private val onPrimaryLight = Color(0xFFFDFFDC)
private val primaryContainerLight = Color(0xFFE2E5C3)
private val onPrimaryContainerLight = Color(0xFF2F321B)
private val secondaryLight = Color(0xFF5F5F53)
private val onSecondaryLight = Color(0xFFFDFFDC)
private val secondaryContainerLight = Color(0xFFE5E4D4)
private val onSecondaryContainerLight = Color(0xFF48493D)
private val tertiaryLight = Color(0xFF6E3D00)
private val onTertiaryLight = Color(0xFFFFDCBF)
private val tertiaryContainerLight = Color(0xFF9E5E12)
private val onTertiaryContainerLight = Color(0xFFFFEEE1)
private val errorLight = Color(0xFFBA1A1A)
private val onErrorLight = Color(0xFFFFEDEA)
private val errorContainerLight = Color(0xFFFFDAD6)
private val onErrorContainerLight = Color(0xFF410002)
private val backgroundLight = Color(0xFFFCF9F5)
private val onBackgroundLight = Color(0xFF1C1C19)
private val surfaceLight = Color(0xFFFCF9F5)
private val onSurfaceLight = Color(0xFF1C1C19)
private val surfaceVariantLight = Color(0xFFE4E3D6)
private val onSurfaceVariantLight = Color(0xFF47473E)
private val outlineLight = Color(0xFF78786D)
private val outlineVariantLight = Color(0xFFC8C7BB)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF31302E)
private val inverseOnSurfaceLight = Color(0xFFF4F0EC)
private val inversePrimaryLight = Color(0xFFC6C9A9)
private val surfaceDimLight = Color(0xFFDDD9D5)
private val surfaceBrightLight = Color(0xFFFCF9F5)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFF7F3EF)
private val surfaceContainerLight = Color(0xFFF1EDE9)
private val surfaceContainerHighLight = Color(0xFFEBE8E3)
private val surfaceContainerHighestLight = Color(0xFFE5E2DE)

private val primaryDark = Color(0xFFC6C9A9)
private val onPrimaryDark = Color(0xFF2F321B)
private val primaryContainerDark = Color(0xFF464930)
private val onPrimaryContainerDark = Color(0xFFA1A485)
private val secondaryDark = Color(0xFFC8C7B8)
private val onSecondaryDark = Color(0xFF303126)
private val secondaryContainerDark = Color(0xFF3D3E33)
private val onSecondaryContainerDark = Color(0xFFD3D2C2)
private val tertiaryDark = Color(0xFFFFB873)
private val onTertiaryDark = Color(0xFF4A2800)
private val tertiaryContainerDark = Color(0xFF7F4800)
private val onTertiaryContainerDark = Color(0xFFFFF8F5)
private val errorDark = Color(0xFFFFB4AB)
private val onErrorDark = Color(0xFF690005)
private val errorContainerDark = Color(0xFF93000A)
private val onErrorContainerDark = Color(0xFFFFDAD6)
private val backgroundDark = Color(0xFF141311)
private val onBackgroundDark = Color(0xFFE5E2DE)
private val surfaceDark = Color(0xFF141311)
private val onSurfaceDark = Color(0xFFE5E2DE)
private val surfaceVariantDark = Color(0xFF47473E)
private val onSurfaceVariantDark = Color(0xFFC8C7BB)
private val outlineDark = Color(0xFF929186)
private val outlineVariantDark = Color(0xFF47473E)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFE5E2DE)
private val inverseOnSurfaceDark = Color(0xFF31302E)
private val inversePrimaryDark = Color(0xFF5D6146)
private val surfaceDimDark = Color(0xFF141311)
private val surfaceBrightDark = Color(0xFF3A3936)
private val surfaceContainerLowestDark = Color(0xFF0E0E0C)
private val surfaceContainerLowDark = Color(0xFF1C1C19)
private val surfaceContainerDark = Color(0xFF20201D)
private val surfaceContainerHighDark = Color(0xFF2A2A27)
private val surfaceContainerHighestDark = Color(0xFF353532)

private val JapanRed = Color(0xFFE03523)

private val defaultLightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val defaultDarkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

val defaultColorSchemes = ColorSchemes(
    light = defaultLightScheme,
    dark = defaultDarkScheme
)
