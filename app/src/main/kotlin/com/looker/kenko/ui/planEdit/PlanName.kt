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

package com.looker.kenko.ui.planEdit

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.kenko.R
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun PlanName(
    planName: TextFieldState,
    onNext: KeyboardActionHandler,
    modifier: Modifier = Modifier,
    error: Boolean = false,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.heading_select_plan_name),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.secondary,
        )
        PlanNameField(state = planName, onNext = onNext)
        val contentColor by animateColorAsState(
            targetValue = if (error) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.outline
            },
        )
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            PlanNameSuggestion {
                if (error) {
                    Text(text = stringResource(R.string.error_plan_name_exists))
                } else {
                    Text(text = stringResource(R.string.desc_select_plan_name))
                }
            }
        }
    }
}

@Composable
private fun PlanNameSuggestion(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            imageVector = KenkoIcons.Info,
            contentDescription = null,
        )
        ProvideTextStyle(
            value = MaterialTheme.typography.labelLarge.copy(lineBreak = LineBreak.Paragraph),
            content = content,
        )
    }
}

@Composable
private fun PlanNameField(
    state: TextFieldState,
    onNext: KeyboardActionHandler,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        state = state,
        modifier = modifier,
        textStyle = MaterialTheme.typography.titleLarge
            .merge(color = LocalContentColor.current),
        cursorBrush = SolidColor(LocalContentColor.current),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next,
        ),
        onKeyboardAction = onNext,
        decorator = {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
            ) {
                it()
                HorizontalDivider()
            }
        },
    )
}

@Preview
@Composable
private fun FieldPreview() {
    KenkoTheme {
        PlanName(
            planName = TextFieldState("Winter Arc"),
            error = false,
            onNext = {},
        )
    }
}
