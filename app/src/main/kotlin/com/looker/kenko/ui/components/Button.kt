package com.looker.kenko.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.theme.KenkoIcons

@Composable
fun KenkoButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    icon: @Composable () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        contentPadding = PaddingValues(
            vertical = 24.dp,
            horizontal = 40.dp
        ),
    ) {
        label()
        Spacer(modifier = Modifier.width(8.dp))
        icon()
    }
}

@Composable
fun SecondaryKenkoButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    icon: @Composable () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        contentPadding = PaddingValues(
            vertical = 24.dp,
            horizontal = 40.dp
        ),
    ) {
        label()
        Spacer(modifier = Modifier.width(8.dp))
        icon()
    }
}

@Composable
fun TertiaryKenkoButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    icon: @Composable () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
        ),
        contentPadding = PaddingValues(
            vertical = 24.dp,
            horizontal = 40.dp
        ),
    ) {
        label()
        Spacer(modifier = Modifier.width(8.dp))
        Box(Modifier.size(18.dp)) {
            icon()
        }
    }
}

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
