package com.rohan.alle.assignment.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope

@Composable
fun RepeatOnLifecycleEffect(
    state: Lifecycle.State = Lifecycle.State.RESUMED,
    action: suspend CoroutineScope.() -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(state, block = action)
    }
}