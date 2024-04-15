package com.looker.kenko.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
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
            KenkoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KenkoNavHost()
//                    Profile()
                }
            }
        }
    }
}
