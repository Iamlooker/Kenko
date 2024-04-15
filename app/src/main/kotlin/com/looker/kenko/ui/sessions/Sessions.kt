package com.looker.kenko.ui.sessions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.FiberManualRecord
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.kenko.R
import com.looker.kenko.data.model.Session
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.utils.DateTimeFormat
import com.looker.kenko.utils.formatDate
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sessions(
    viewModel: SessionsViewModel,
    onSessionClick: (LocalDate?) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.inverseSurface,
                elevation = FloatingActionButtonDefaults.loweredElevation(),
                onClick = { onSessionClick(null) }
            ) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { /*TODO*/ },
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.FiberManualRecord,
                            contentDescription = null
                        )
                    }
                )
            }
        },
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