package com.looker.kenko.ui.sessions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.kenko.data.model.Session
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.utils.DateTimeFormat
import com.looker.kenko.utils.formatDate
import kotlinx.datetime.LocalDate

@Composable
fun Sessions(
    viewModel: SessionsViewModel,
    onSessionClick: (LocalDate?) -> Unit,
) {
    Scaffold(
        modifier = Modifier.padding(bottom = 80.dp),
        floatingActionButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                ),
                shape = MaterialTheme.shapes.extraLarge,
                onClick = { onSessionClick(null) },
                contentPadding = PaddingValues(vertical = 32.dp, horizontal = 48.dp)
            ) {
                Text(text = "Start")
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = KenkoIcons.ArrowForward,
                    contentDescription = ""
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { padding ->
        val sessions by viewModel.sessionsStream.collectAsState()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(
                items = sessions,
                key = { it.date.toEpochDays() },
                contentType = { it.date.dayOfWeek }
            ) { session ->
                SessionCard(
                    session = session,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    onClick = { onSessionClick(session.date) }
                )
            }
        }
    }
}

@Composable
fun SessionCard(
    session: Session,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 18.dp, vertical = 28.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = session.formattedDate(),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            SessionIcon(
                dayOfWeek = session.date.dayOfWeek,
                modifier = Modifier.size(72.dp)
            )
        }
    }
}

@Composable
fun LocalDate.format(
    format: DateTimeFormat = DateTimeFormat.Short,
): String = remember(this) {
    formatDate(this, dateTimeFormat = format)
}

@Composable
fun Session.formattedDate(format: DateTimeFormat = DateTimeFormat.Short): String =
    remember(date) {
        formatDate(date, dateTimeFormat = format)
    }

@Preview
@Composable
private fun SessionCardPreview() {
    KenkoTheme {
        SessionCard(
            session = Session.SAMPLE,
            modifier = Modifier.fillMaxWidth()
        )
    }
}