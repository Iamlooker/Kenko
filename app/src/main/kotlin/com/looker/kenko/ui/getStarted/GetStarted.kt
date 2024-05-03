package com.looker.kenko.ui.getStarted

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.looker.kenko.R
import com.looker.kenko.ui.helper.vertical
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun GetStarted(onNext: () -> Unit) {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 16.dp),
                imageVector = KenkoIcons.Dawn,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
            )
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(y = 48.dp),
                imageVector = KenkoIcons.QuarterCircles,
                tint = MaterialTheme.colorScheme.outlineVariant,
                contentDescription = null,
            )
            HeroTitle(modifier = Modifier.align(Alignment.CenterStart))
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ButtonGroup(
                    onClick = onNext
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "health is in your hands",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun ButtonGroup(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Layout(
        modifier = modifier,
        content = {
            Icon(
                modifier = Modifier.layoutId(ButtonID.Cloud),
                imageVector = KenkoIcons.Cloud,
                contentDescription = null
            )
            Icon(
                modifier = Modifier.layoutId(ButtonID.Arrow1),
                imageVector = KenkoIcons.Arrow1,
                contentDescription = null
            )
            Icon(
                modifier = Modifier.layoutId(ButtonID.Arrow2),
                imageVector = KenkoIcons.Arrow2,
                contentDescription = null
            )
            Icon(
                modifier = Modifier.layoutId(ButtonID.Arrow3),
                imageVector = KenkoIcons.Arrow3,
                contentDescription = null
            )
            Icon(
                modifier = Modifier.layoutId(ButtonID.Arrow4),
                imageVector = KenkoIcons.Arrow4,
                contentDescription = null
            )
            Button(
                modifier = Modifier.layoutId(ButtonID.Button),
                onClick = onClick,
                contentPadding = PaddingValues(vertical = 32.dp, horizontal = 48.dp)
            ) {
                Icon(
                    imageVector = KenkoIcons.ArrowForward,
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.label_lets_go))
            }
        }
    ) { measurables, constraints ->
        lateinit var cloud: Measurable
        lateinit var arrow1: Measurable
        lateinit var arrow2: Measurable
        lateinit var arrow3: Measurable
        lateinit var arrow4: Measurable
        lateinit var button: Measurable
        measurables.forEach { measurable ->
            when (measurable.layoutId) {
                ButtonID.Button -> button = measurable
                ButtonID.Cloud -> cloud = measurable
                ButtonID.Arrow1 -> arrow1 = measurable
                ButtonID.Arrow2 -> arrow2 = measurable
                ButtonID.Arrow3 -> arrow3 = measurable
                ButtonID.Arrow4 -> arrow4 = measurable
                else -> error("Unknown Element")
            }
        }
        val cloudPlaceable = cloud.measure(constraints)
        val arrow1Placeable = arrow1.measure(constraints)
        val arrow2Placeable = arrow2.measure(constraints)
        val arrow3Placeable = arrow3.measure(constraints)
        val arrow4Placeable = arrow4.measure(constraints)
        val buttonPlaceable = button.measure(constraints)
        val width = 360.dp.toPx().toInt()
        layout(width, 162.dp.toPx().toInt()) {
            val x = (width / 2) - (buttonPlaceable.width / 2)
            cloudPlaceable.placeRelative(16.dp.toPx().toInt(), 31.dp.toPx().toInt())
            arrow1Placeable.placeRelative(148.dp.toPx().toInt(), 2.dp.toPx().toInt())
            arrow2Placeable.placeRelative(200.dp.toPx().toInt(), 0.dp.toPx().toInt())
            arrow3Placeable.placeRelative(270.dp.toPx().toInt(), 26.dp.toPx().toInt())
            arrow4Placeable.placeRelative(290.dp.toPx().toInt(), 100.dp.toPx().toInt())
            buttonPlaceable.placeRelative(x, 68.dp.toPx().toInt())
        }
    }
}

@Composable
private fun HeroTitle(modifier: Modifier = Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy((-12).dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                modifier = Modifier.vertical(),
                text = stringResource(R.string.label_kenko),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            AsyncImage(
                modifier = Modifier
                    .width(48.dp)
                    .aspectRatio(9F / 16F)
                    .clip(CircleShape),
                model = "https://source.unsplash.com/kFCdfLbu6zA/563x1000",
                contentDescription = null,
            )
        }
        Column {
            Text(
                text = stringResource(R.string.label_kenko_jp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = stringResource(R.string.label_kenko_meaning),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = stringResource(R.string.label_kenko_meaning_ALT),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

enum class ButtonID {
    Cloud, Arrow1, Arrow2, Arrow3, Arrow4, Button
}

@Preview
@Composable
private fun HeroPreview() {
    KenkoTheme {
        ButtonGroup(onClick = {})
    }
}
