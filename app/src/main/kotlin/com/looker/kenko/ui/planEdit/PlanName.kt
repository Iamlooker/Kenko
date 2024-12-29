package com.looker.kenko.ui.planEdit

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        PlanNameSuggestion()
    }
}

@Composable
private fun PlanNameSuggestion(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            imageVector = KenkoIcons.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
        )
        Text(
            text = stringResource(R.string.desc_select_plan_name),
            style = MaterialTheme.typography.labelLarge.merge(
                lineBreak = LineBreak.Paragraph,
                color = MaterialTheme.colorScheme.outline,
            ),
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
        }
    )
}

@Preview
@Composable
private fun FieldPreview() {
    KenkoTheme {
        PlanName(
            planName = TextFieldState("Winter Arc"),
            onNext = {}
        )
    }
}
