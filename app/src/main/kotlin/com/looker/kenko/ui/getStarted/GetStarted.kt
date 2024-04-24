package com.looker.kenko.ui.getStarted

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.looker.kenko.R

@Composable
fun GetStarted(onNext: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Star(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(380.dp)
                .offset(x = 140.dp)
                .alpha(0.7F)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            val name = stringResource(R.string.label_kenko)
            val katanaga = stringResource(R.string.label_kenko_jp)
            val title = buildAnnotatedString {
                withStyle(MaterialTheme.typography.displayLarge.toSpanStyle()) {
                    append(name)
                }
                append("  ")
                withStyle(
                    MaterialTheme.typography.labelSmall.toSpanStyle()
                        .copy(MaterialTheme.colorScheme.outline)
                ) {
                    append(katanaga)
                }
            }
            Spacer(modifier = Modifier.weight(1F))
            Text(text = title)
            Text(
                text = stringResource(R.string.label_kenko_meaning),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.label_kenko_meaning_ALT),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(40.dp))
            FilledTonalButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(12.dp)
                    .navigationBarsPadding(),
                onClick = onNext,
                contentPadding = PaddingValues(vertical = 32.dp, horizontal = 48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = stringResource(R.string.label_lets_go))
            }
        }
    }
}