package com.looker.kenko.ui.planEdit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun DaySelector() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .clip(CircleShape)
    ) {

    }
}

@Preview
@Composable
private fun DaySelectorPreview() {
    KenkoTheme {
        DaySelector()
    }
}
