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

package com.looker.kenko.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.ui.getStarted.navigation.GetStartedRoute
import com.looker.kenko.ui.home.navigation.HomeRoute
import com.looker.kenko.ui.navigation.KenkoNavHost
import com.looker.kenko.ui.theme.KenkoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val theme by viewModel.theme.collectAsStateWithLifecycle()
            val colorScheme by viewModel.colorScheme.collectAsStateWithLifecycle()
            val appState = rememberKenkoAppState()
            KenkoTheme(
                theme = theme,
                colorSchemes = colorScheme,
            ) {
                Kenko(appState) {
                    KenkoNavHost(
                        appState = appState,
                        startDestination = if (viewModel.isOnboardingDone) {
                            HomeRoute
                        } else {
                            GetStartedRoute
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun Kenko(
    appState: KenkoAppState,
    content: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            AnimatedVisibility(
                visible = appState.isTopLevelDestination,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
            ) {
                NavigationBar {
                    appState.topLevelDestinations.forEach { destination ->
                        NavigationBarItem(
                            selected = destination == appState.currentTopLevelDestination,
                            onClick = { appState.navigateToTopLevelDestination(destination) },
                            icon = {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = null,
                                )
                            },
                            label = { Text(text = stringResource(destination.labelRes)) },
                            alwaysShowLabel = false,
                            modifier = Modifier.weight(1F),
                        )
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets(0),
        content = content,
    )
}
