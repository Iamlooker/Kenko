package com.looker.kenko.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

private const val SHARING_STARTED_DEFAULT = 5_000L

context(ViewModel)
fun <T> Flow<T>.asStateFlow(
    initial: T,
    coroutineScope: CoroutineScope = viewModelScope,
    started: SharingStarted = SharingStarted.WhileSubscribed(SHARING_STARTED_DEFAULT)
): StateFlow<T> = stateIn(
    scope = coroutineScope,
    started = started,
    initialValue = initial
)
