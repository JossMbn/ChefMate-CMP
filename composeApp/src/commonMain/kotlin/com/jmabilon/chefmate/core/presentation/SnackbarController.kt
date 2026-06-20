package com.jmabilon.chefmate.core.presentation

import com.jmabilon.chefmate.core.presentation.extension.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class SnackbarData(
    val message: UiText
)

object SnackbarController {

    private val _event = Channel<SnackbarData>()
    val event = _event.receiveAsFlow()

    suspend fun sendError(error: Throwable) {
        _event.send(SnackbarData(message = error.toUiText()))
    }
}
