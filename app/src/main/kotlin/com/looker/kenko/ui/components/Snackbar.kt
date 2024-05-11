package com.looker.kenko.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun ErrorSnackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier,
) {
    Snackbar(
        modifier = modifier.height(96.dp),
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.onError,
        dismissActionContentColor = MaterialTheme.colorScheme.onError,
        snackbarData = data,
    )
}

@PreviewLightDark
@Composable
private fun SnackbarPreview() {
    KenkoTheme {
        ErrorSnackbar(data = object : SnackbarData {
            override val visuals: SnackbarVisuals
                get() = object : SnackbarVisuals {
                    override val actionLabel: String?
                        get() = null
                    override val duration: SnackbarDuration
                        get() = SnackbarDuration.Long
                    override val message: String
                        get() = "Error"
                    override val withDismissAction: Boolean
                        get() = false

                }

            override fun dismiss() {}

            override fun performAction() {}
        })
    }
}
