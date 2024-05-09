package com.looker.kenko.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.looker.kenko.ui.navigation.KenkoNavHost
import com.looker.kenko.ui.theme.KenkoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            Kenko()
        }
    }
}

@Composable
fun Kenko() {
    KenkoTheme {
        val appState = rememberKenkoAppState()
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
                            NavigationRailItem(
                                selected = destination == appState.currentTopLevelDestination,
                                onClick = { appState.navigateToTopLevelDestination(destination) },
                                icon = {
                                    Icon(
                                        imageVector = destination.icon,
                                        contentDescription = null
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
            contentWindowInsets = WindowInsets(0)
        ) {
            KenkoNavHost(appState)
        }
    }
}
