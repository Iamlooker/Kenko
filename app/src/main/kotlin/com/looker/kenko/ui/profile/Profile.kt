package com.looker.kenko.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.profile.component.InfoCard
import com.looker.kenko.ui.profile.component.ProfileImage

// TODO: Replace with placeholder
private const val PROFILE_URL = "https://source.unsplash.com/kFCdfLbu6zA/500x500"

@Composable
fun Profile() {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item(span = { GridItemSpan(2) }) {
            ProfileImage(
                modifier = Modifier.size(300.dp),
                imagePath = PROFILE_URL
            )
        }

        item(span = { GridItemSpan(2) }) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Silfie Rost",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.headlineLarge,
            )
        }

        item {
            InfoCard(
                modifier = Modifier.fillMaxSize(),
                title = "Streak"
            ) {
                Text(text = "147")
            }
        }

        item {
            InfoCard(
                modifier = Modifier.fillMaxSize(),
                title = "Lifts",
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Text(text = "1024")
            }
        }

        item(span = { GridItemSpan(2) }) {
            FilledTonalIconButton(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(2.2F),
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                ),
                shape = MaterialTheme.shapes.large,
                onClick = { /*TODO*/ }
            ) {
                Icon(imageVector = Icons.Rounded.Person, contentDescription = null)
            }
        }
    }
}
