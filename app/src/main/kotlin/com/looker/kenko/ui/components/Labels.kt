package com.looker.kenko.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.looker.kenko.R

@Composable
fun LiftingQuotes(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    style: TextStyle = MaterialTheme.typography.labelSmall,
) {
    val array = stringArrayResource(R.array.label_lifting_quotes)
    val randomQuote = remember {
        array.random()
    }
    Text(
        text = randomQuote,
        style = style,
        color = color,
        modifier = Modifier
            .padding(top = 8.dp, bottom = 6.dp)
            .then(modifier),
    )
}

@Composable
fun HealthQuotes(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    style: TextStyle = MaterialTheme.typography.labelSmall,
) {
    val array = stringArrayResource(R.array.label_health_quotes)
    val randomQuote = remember {
        array.random()
    }
    Text(
        text = randomQuote,
        style = style,
        color = color,
        modifier = Modifier
            .padding(top = 8.dp, bottom = 6.dp)
            .then(modifier),
    )
}
