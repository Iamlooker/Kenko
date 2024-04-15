package com.looker.kenko.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChangeHistory
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.looker.kenko.R
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun PRLabel(
    modifier: Modifier = Modifier
) {
    Badge(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.tertiary
    ) {
       Icon(imageVector = Icons.Rounded.ChangeHistory, contentDescription = null)
       Text(text = stringResource(R.string.label_record))
    }
}

@PreviewLightDark
@Composable
private fun LabelPreview() {
    KenkoTheme {
        PRLabel()
    }
}