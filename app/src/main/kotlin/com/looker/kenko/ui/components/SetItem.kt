package com.looker.kenko.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.looker.kenko.R
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.numbers

@Composable
fun SetItem(
    title: String,
    rep: Int,
    weight: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .heightIn(64.dp)
            .widthIn(240.dp, 420.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = title,
            style = MaterialTheme.typography.displayMedium.numbers(),
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.width(12.dp))
        Row(
            modifier = Modifier
                .weight(1F)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(vertical = 16.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PerformedItem(title = stringResource(R.string.label_reps), performance = "$rep")
            PerformedItem(title = stringResource(R.string.label_weight), performance = "$weight KG")
        }
    }
}

@Composable
fun PerformedItem(
    title: String,
    performance: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = performance,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@PreviewScreenSizes
@Composable
private fun SetItemPreview() {
    KenkoTheme {
        SetItem("01", 14, 45.0)
    }
}