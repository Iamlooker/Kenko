package com.looker.kenko.ui.settings

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SingleChoiceSegmentedButtonRowScope
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.model.settings.ColorPalettes
import com.looker.kenko.data.model.settings.Theme
import com.looker.kenko.ui.components.AnimatedWave
import com.looker.kenko.ui.components.BackButton
import com.looker.kenko.ui.components.HealthQuotes
import com.looker.kenko.ui.components.KenkoBorderWidth
import com.looker.kenko.ui.components.texture.GradientStart
import com.looker.kenko.ui.components.texture.gradient
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.colorSchemes.defaultColorSchemes
import com.looker.kenko.ui.theme.colorSchemes.sereneColorSchemes
import com.looker.kenko.ui.theme.dynamicColorSchemes
import com.looker.kenko.ui.theme.end
import com.looker.kenko.ui.theme.start

@Composable
fun Settings(
    viewModel: SettingsViewModel,
    onBackPress: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Settings(
        state = state,
        onSelectTheme = viewModel::updateTheme,
        onSelectColorPalette = viewModel::updateColorPalette,
        onBackPress = onBackPress,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Settings(
    state: SettingsUiData,
    onSelectTheme: (Theme) -> Unit,
    onSelectColorPalette: (ColorPalettes) -> Unit,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .gradient(
                0F to MaterialTheme.colorScheme.secondaryContainer.copy(0.2F),
                1F to MaterialTheme.colorScheme.background,
                start = GradientStart.Custom {
                    Offset(x = size.width - 100F, y = 150F)
                },
            ),
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = { BackButton(onClick = onBackPress) },
                title = { Text(text = stringResource(R.string.label_settings)) },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState()),
        ) {
            HorizontalDivider(thickness = KenkoBorderWidth)
            Spacer(modifier = Modifier.height(16.dp))
            CategoryHeader(title = stringResource(R.string.label_theme))
            Spacer(modifier = Modifier.height(4.dp))
            ThemeButton(
                modifier = Modifier.align(CenterHorizontally),
                selectedTheme = state.selectedTheme,
                onClick = onSelectTheme,
            )
            Spacer(modifier = Modifier.height(16.dp))
            CategoryHeader(title = stringResource(R.string.label_color_palettes))
            Spacer(modifier = Modifier.height(8.dp))
            ColorPaletteSelection(
                selectedColorPalette = state.selectedColorPalette,
                selectedTheme = state.selectedTheme,
                onClickPalette = onSelectColorPalette
            )
            Spacer(modifier = Modifier.weight(1F))
            HealthQuotes(Modifier.align(CenterHorizontally))
        }
    }
}

@Composable
private fun CategoryHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.width(4.dp))
        AnimatedWave(
            modifier = Modifier.fillMaxWidth(),
            amplitude = 8f,
            durationMillis = 5_000,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
private fun ColorPaletteSelection(
    selectedColorPalette: ColorPalettes,
    selectedTheme: Theme,
    onClickPalette: (ColorPalettes) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        ColorPalettes.entries.forEach { colorPalette ->
            ColorPaletteItem(
                isSelected = selectedColorPalette == colorPalette,
                theme = selectedTheme,
                colorPalette = colorPalette,
                modifier = Modifier.clickable { onClickPalette(colorPalette) },
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
private fun ColorPaletteItem(
    isSelected: Boolean,
    theme: Theme,
    colorPalette: ColorPalettes,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val colorSchemes = remember(colorPalette) {
        colorPalette.scheme ?: dynamicColorSchemes(context) ?: defaultColorSchemes
    }
    val transition = updateTransition(targetState = isSelected, label = null)
    val corner by transition.animateDp(label = "") {
        if (it) 32.dp else 16.dp
    }
    val background by transition.animateColor(label = "") {
        if (it) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainerHigh
    }
    val contentColor by transition.animateColor(label = "") {
        if (it) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurface
    }
    Column(
        modifier = Modifier
            .graphicsLayer {
                clip = true
                shape = RoundedCornerShape(corner, corner, 16.dp, 16.dp)
            }
            .drawBehind { drawRect(background) }
            .then(modifier),
        horizontalAlignment = CenterHorizontally,
    ) {
        KenkoTheme(
            theme = theme,
            colorSchemes = colorSchemes
        ) {
            Box(Modifier.size(80.dp)) {
                ColorPaletteSample()
                Crossfade(targetState = isSelected, label = "") {
                    if (it) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = KenkoIcons.Done,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
        Text(
            modifier = Modifier.padding(vertical = 2.dp),
            text = stringResource(colorSchemes.nameRes),
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
        )
    }
}

@Composable
private fun ColorPaletteSample(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .then(modifier)
            .padding(8.dp)
            .clip(CircleShape)
    ) {
        Spacer(
            modifier = Modifier
                .size(31.dp)
                .align(Alignment.TopStart)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
        )
        Spacer(
            modifier = Modifier
                .size(31.dp)
                .align(Alignment.TopEnd)
                .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(4.dp))
        )
        Spacer(
            modifier = Modifier
                .size(64.dp, 31.dp)
                .align(Alignment.BottomStart)
                .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(4.dp))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeButton(
    selectedTheme: Theme,
    onClick: (Theme) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        val isSystem = remember(selectedTheme) { selectedTheme == Theme.System }
        val isDark = remember(selectedTheme) { selectedTheme == Theme.Dark }
        val isLight = remember(selectedTheme) { selectedTheme == Theme.Light }
        SystemButton(isSelected = isSystem, onClick = onClick)
        LightButton(isSelected = isLight, onClick = onClick)
        DarkButton(isSelected = isDark, onClick = onClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingleChoiceSegmentedButtonRowScope.SystemButton(
    isSelected: Boolean,
    onClick: (Theme) -> Unit,
) {
    val theme = Theme.System
    SegmentedButton(
        selected = isSelected,
        onClick = { onClick(theme) },
        shape = CircleShape.end(4.dp),
        colors = themeButtonColors,
        modifier = Modifier.padding(2.dp),
    ) {
        Text(text = stringResource(theme.nameRes))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingleChoiceSegmentedButtonRowScope.LightButton(
    isSelected: Boolean,
    onClick: (Theme) -> Unit,
) {
    val theme = Theme.Light
    SegmentedButton(
        selected = isSelected,
        onClick = { onClick(theme) },
        shape = RoundedCornerShape(4.dp),
        colors = themeButtonColors,
        modifier = Modifier.padding(2.dp),
    ) {
        Text(text = stringResource(theme.nameRes))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingleChoiceSegmentedButtonRowScope.DarkButton(
    isSelected: Boolean,
    onClick: (Theme) -> Unit,
) {
    val theme = Theme.Dark
    SegmentedButton(
        selected = isSelected,
        onClick = { onClick(theme) },
        shape = CircleShape.start(4.dp),
        colors = themeButtonColors,
        modifier = Modifier.padding(2.dp),
    ) {
        Text(text = stringResource(theme.nameRes))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private val themeButtonColors: SegmentedButtonColors
    @Composable
    get() = SegmentedButtonDefaults.colors(
        activeBorderColor = Color.Transparent,
        inactiveBorderColor = Color.Transparent,
        inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    )

@Preview
@Composable
private fun ColorSelectionPreview() {
    KenkoTheme(colorSchemes = sereneColorSchemes) {
        var isSelected by remember { mutableStateOf(false) }
        ColorPaletteItem(
            modifier = Modifier.clickable {
                isSelected = !isSelected
            },
            isSelected = isSelected,
            theme = Theme.System,
            colorPalette = ColorPalettes.Serene
        )
    }
}

@Preview
@Composable
private fun ThemePreview() {
    KenkoTheme {
        ThemeButton(selectedTheme = Theme.System, onClick = {})
    }
}

@Preview
@Composable
private fun SettingsPreview() {
    KenkoTheme {
        Settings(
            state = SettingsUiData(Theme.System, ColorPalettes.Default),
            onSelectTheme = {},
            onSelectColorPalette = {},
            onBackPress = {},
        )
    }
}
