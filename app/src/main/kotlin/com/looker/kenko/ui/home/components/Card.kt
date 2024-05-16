package com.looker.kenko.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.components.texture.GradientStart
import com.looker.kenko.ui.components.texture.gradient
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun ProminentCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .height(140.dp)
            .width(IntrinsicSize.Min)
            .clip(shape)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .gradient(
                0f to MaterialTheme.colorScheme.primary,
                1f to Color.Transparent,
                start =  GradientStart.TopRight,
            )
            .padding(16.dp)
            .clickable(onClick = onClick),
    ) {
        Icon(
            modifier = Modifier.align(Alignment.TopEnd),
            imageVector = KenkoIcons.ArrowOutwardLarge, contentDescription = null)
        content()
    }
}

@Composable
fun ActionCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {

}

@Preview
@Composable
private fun ProminentCardPreview() {
    KenkoTheme {
        ProminentCard(onClick = {}) {
            Text(text = "Select Plan", style = MaterialTheme.typography.displayMedium)
        }
    }
}

@Preview
@Composable
private fun ActionCardPreview() {
    KenkoTheme {
        ActionCard {

        }
    }
}
