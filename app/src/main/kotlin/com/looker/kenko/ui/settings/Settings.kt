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

package com.looker.kenko.ui.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SingleChoiceSegmentedButtonRowScope
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.model.settings.BackupInterval
import com.looker.kenko.data.model.settings.ColorPalettes
import com.looker.kenko.data.model.settings.Theme
import com.looker.kenko.ui.components.BackButton
import com.looker.kenko.ui.components.HealthQuotes
import com.looker.kenko.ui.components.KenkoBorderWidth
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.colorSchemes.sereneColorSchemes
import com.looker.kenko.ui.theme.dynamicColorSchemes
import com.looker.kenko.ui.theme.end
import com.looker.kenko.ui.theme.start
import com.looker.kenko.utils.toFormat
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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
        onSelectBackupLocation = viewModel::setBackupLocation,
        onSelectBackupInterval = viewModel::setBackupInterval,
        onBackupNow = viewModel::backupNow,
        onRestore = viewModel::restore,
        onClearMessage = viewModel::clearBackupMessage,
        onBackPress = onBackPress,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Settings(
    state: SettingsUiData,
    onSelectTheme: (Theme) -> Unit,
    onSelectColorPalette: (ColorPalettes) -> Unit,
    onSelectBackupLocation: (Uri) -> Unit,
    onSelectBackupInterval: (BackupInterval) -> Unit,
    onBackupNow: () -> Unit,
    onRestore: (Uri) -> Unit,
    onClearMessage: () -> Unit,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Handle backup messages
    LaunchedEffect(state.backupMessage) {
        state.backupMessage?.let { message ->
            val text = when (message) {
                BackupMessage.BackupSuccess -> context.getString(R.string.label_backup_success)
                BackupMessage.BackupFailed -> context.getString(R.string.error_backup_failed)
                BackupMessage.RestoreSuccess -> context.getString(R.string.label_restore_success)
                BackupMessage.RestoreFailed -> context.getString(R.string.error_restore_failed)
            }
            snackbarHostState.showSnackbar(text)
            onClearMessage()
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = { BackButton(onClick = onBackPress) },
                title = { Text(text = stringResource(R.string.label_settings)) },
            )
        },
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
                onClickPalette = onSelectColorPalette,
            )
            Spacer(modifier = Modifier.height(24.dp))
            CategoryHeader(title = stringResource(R.string.label_backup))
            Spacer(modifier = Modifier.height(8.dp))
            BackupSection(
                backupUri = state.backupUri,
                backupInterval = state.backupInterval,
                lastBackupTime = state.lastBackupTime,
                isBackingUp = state.isBackingUp,
                isRestoring = state.isRestoring,
                onSelectLocation = onSelectBackupLocation,
                onSelectInterval = onSelectBackupInterval,
                onBackupNow = onBackupNow,
                onRestore = onRestore,
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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.width(4.dp))
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
        colorPalette.scheme ?: dynamicColorSchemes(context)
    }
    if (colorSchemes == null) return
    val transition = updateTransition(targetState = isSelected, label = null)
    val corner by transition.animateDp(label = "") {
        if (it) 32.dp else 16.dp
    }
    val background by transition.animateColor(label = "") {
        if (it) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        }
    }
    val contentColor by transition.animateColor(label = "") {
        if (it) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        }
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
            colorSchemes = colorSchemes,
        ) {
            Box(Modifier.size(80.dp)) {
                ColorPaletteSample()
                Crossfade(targetState = isSelected, label = "") {
                    if (it) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painter = KenkoIcons.Done,
                                contentDescription = null,
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
            .clip(CircleShape),
    ) {
        Spacer(
            modifier = Modifier
                .size(31.dp)
                .align(Alignment.TopStart)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)),
        )
        Spacer(
            modifier = Modifier
                .size(31.dp)
                .align(Alignment.TopEnd)
                .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(4.dp)),
        )
        Spacer(
            modifier = Modifier
                .size(64.dp, 31.dp)
                .align(Alignment.BottomStart)
                .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(4.dp)),
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

@Composable
private fun BackupSection(
    backupUri: String?,
    backupInterval: BackupInterval,
    lastBackupTime: Instant?,
    isBackingUp: Boolean,
    isRestoring: Boolean,
    onSelectLocation: (Uri) -> Unit,
    onSelectInterval: (BackupInterval) -> Unit,
    onBackupNow: () -> Unit,
    onRestore: (Uri) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var showRestoreDialog by remember { mutableStateOf(false) }
    var pendingRestoreUri by remember { mutableStateOf<Uri?>(null) }

    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
    ) { uri ->
        uri?.let {
            // Take persistable permission
            context.contentResolver.takePersistableUriPermission(
                it,
                android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION,
            )
            // Store the tree URI - file will be created by BackupManager
            onSelectLocation(it)
        }
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        uri?.let {
            pendingRestoreUri = it
            showRestoreDialog = true
        }
    }

    if (showRestoreDialog) {
        RestoreConfirmationDialog(
            onConfirm = {
                pendingRestoreUri?.let { onRestore(it) }
                showRestoreDialog = false
                pendingRestoreUri = null
            },
            onDismiss = {
                showRestoreDialog = false
                pendingRestoreUri = null
            },
        )
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        BackupSettingRow(
            title = stringResource(R.string.label_backup_location),
            value = backupUri?.let { extractFolderName(it) }
                ?: stringResource(R.string.label_backup_location_not_set),
            onClick = { folderPickerLauncher.launch(null) },
        )

        Text(
            text = stringResource(R.string.label_backup_interval),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        BackupIntervalSelector(
            selectedInterval = backupInterval,
            onSelectInterval = onSelectInterval,
            enabled = backupUri != null,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        if (lastBackupTime != null) {

            Text(
                text = stringResource(R.string.label_last_backup, lastBackupTime.toFormat()),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            OutlinedButton(
                onClick = onBackupNow,
                enabled = backupUri != null && !isBackingUp && !isRestoring,
                modifier = Modifier.weight(1f),
            ) {
                if (isBackingUp) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = if (isBackingUp) {
                        stringResource(R.string.label_backup_in_progress)
                    } else {
                        stringResource(R.string.label_backup_now)
                    },
                )
            }

            OutlinedButton(
                onClick = { filePickerLauncher.launch(arrayOf("application/zip")) },
                enabled = !isBackingUp && !isRestoring,
                modifier = Modifier.weight(1f),
            ) {
                if (isRestoring) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = if (isRestoring) {
                        stringResource(R.string.label_restore_in_progress)
                    } else {
                        stringResource(R.string.label_restore)
                    },
                )
            }
        }
    }
}

@Composable
private fun BackupSettingRow(
    title: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
            )
        }
        Icon(
            painter = KenkoIcons.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BackupIntervalSelector(
    selectedInterval: BackupInterval,
    onSelectInterval: (BackupInterval) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        BackupInterval.entries.forEachIndexed { index, interval ->
            SegmentedButton(
                selected = selectedInterval == interval,
                onClick = { onSelectInterval(interval) },
                enabled = enabled,
                shape = when (index) {
                    0 -> CircleShape.end(4.dp)
                    BackupInterval.entries.lastIndex -> CircleShape.start(4.dp)
                    else -> RoundedCornerShape(4.dp)
                },
                colors = themeButtonColors,
                modifier = Modifier.padding(2.dp),
            ) {
                Text(
                    text = stringResource(interval.nameRes),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Composable
private fun RestoreConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.label_restore)) },
        text = { Text(stringResource(R.string.label_restore_warning)) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(R.string.label_yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.label_no))
            }
        },
    )
}

private fun extractFolderName(uri: String): String {
    return try {
        uri.toUri().lastPathSegment?.substringAfterLast('/') ?: uri
    } catch (_: Exception) {
        uri
    }
}

private fun formatBackupTime(instant: Instant): String {
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${localDateTime.date} ${localDateTime.hour}:${
        localDateTime.minute.toString().padStart(2, '0')
    }"
}

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
            colorPalette = ColorPalettes.Serene,
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
            state = SettingsUiData(
                selectedTheme = Theme.System,
                selectedColorPalette = ColorPalettes.Default,
                backupUri = null,
                backupInterval = BackupInterval.Off,
                lastBackupTime = null,
                isBackingUp = false,
                isRestoring = false,
                backupMessage = null,
            ),
            onSelectTheme = {},
            onSelectColorPalette = {},
            onSelectBackupLocation = {},
            onSelectBackupInterval = {},
            onBackupNow = {},
            onRestore = {},
            onClearMessage = {},
            onBackPress = {},
        )
    }
}

@Preview
@Composable
private fun BackupSectionPreview() {
    KenkoTheme {
        BackupSection(
            backupUri = "content://com.android.providers.downloads/tree/downloads",
            backupInterval = BackupInterval.Daily,
            lastBackupTime = null,
            isBackingUp = false,
            isRestoring = false,
            onSelectLocation = {},
            onSelectInterval = {},
            onBackupNow = {},
            onRestore = {},
        )
    }
}
