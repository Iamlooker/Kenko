package com.looker.kenko.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.looker.kenko.ui.theme.KenkoIcons

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
) {
    IconButton(
        modifier = modifier,
        colors = colors,
        onClick = onClick,
    ) {
        Icon(imageVector = KenkoIcons.ArrowBack, contentDescription = null)
    }
}