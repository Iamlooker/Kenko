package com.looker.kenko.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.data.model.sampleExercises
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.ui.navigation.KenkoNavHost
import com.looker.kenko.ui.theme.KenkoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repo: ExerciseRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            MuscleGroups.entries.flatMap { it.sampleExercises }.forEach {
                repo.upsert(it)
            }
        }
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
                if (appState.isTopLevelDestination) {
                    NavigationBar {
                        appState.topLevelDestinations.forEach { destination ->
                            NavigationRailItem(
                                selected = destination == appState.currentTopLevelDesination,
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
            contentWindowInsets = if (appState.isTopLevelDestination) WindowInsets.systemBars
            else WindowInsets(0)
        ) {
            KenkoNavHost(appState, modifier = Modifier.padding(it))
        }
    }
}
