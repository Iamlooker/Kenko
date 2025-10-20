/*
 * Copyright (C) 2025 LooKeR & Contributors
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.looker.kenko.ui.components

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun ErrorSnackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier,
) {
    Snackbar(
        modifier = modifier.heightIn(84.dp),
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
        ErrorSnackbar(
            data = object : SnackbarData {
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
            }
        )
    }
}
