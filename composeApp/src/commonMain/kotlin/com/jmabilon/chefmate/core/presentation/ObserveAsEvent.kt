package com.jmabilon.chefmate.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

/**
 * A helper composable that collects a Flow of events and triggers a callback for each event.
 * The collection is tied to the lifecycle of the composable, ensuring that events are only
 * collected when the composable is active.
 *
 * @param events The Flow of events to collect.
 * @param minActiveState The minimum lifecycle state at which to start collecting events. Default is STARTED.
 * @param onEvent The callback to trigger for each collected event.
 */
@Composable
fun <T> ObserveAsEvent(
    events: Flow<T>,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    onEvent: suspend (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(events, lifecycleOwner, minActiveState) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            events.collect { onEvent(it) }
        }
    }
}
