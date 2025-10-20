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
