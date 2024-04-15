package com.looker.kenko.utils

import androidx.core.os.bundleOf
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras

fun savedStateExtras(
    vararg pairs: Pair<String, Any?>,
    default: CreationExtras = CreationExtras.Empty,
): CreationExtras =
    MutableCreationExtras(default).apply {
        set(DEFAULT_ARGS_KEY, bundleOf(*pairs))
    }