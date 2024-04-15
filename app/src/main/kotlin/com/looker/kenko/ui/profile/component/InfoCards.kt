package com.looker.kenko.ui.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.components.AnimatedWave
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.numbers

@Composable
fun InfoCard(
    title: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = contentColorFor(backgroundColor = containerColor),
    shape: Shape = MaterialTheme.shapes.large,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .clip(shape)
            .background(containerColor)
            .padding(16.dp)
            .then(modifier)
            .aspectRatio(2.2F),
        verticalArrangement = Arrangement.Center,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalTextStyle provides MaterialTheme.typography.headlineMedium.numbers(),
        ) {
            Column(
                modifier = Modifier.width(IntrinsicSize.Min),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(2.dp))
                AnimatedWave(
                    modifier = Modifier.fillMaxWidth(),
                    amplitude = 5F,
                    frequency = 0.1F,
                    durationMillis = 5_000,
                    color = LocalContentColor.current,
                )
            }
            content()
        }
    }
}

@PreviewLightDark
@Composable
private fun InfoCardPreview() {
    KenkoTheme {
        InfoCard(title = "Streak", modifier = Modifier.width(180.dp)) {
            Text(text = "147", style = MaterialTheme.typography.headlineMedium)
        }
    }
}
