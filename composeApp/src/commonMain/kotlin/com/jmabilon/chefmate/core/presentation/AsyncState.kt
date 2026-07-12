package com.jmabilon.chefmate.core.presentation

import androidx.compose.runtime.Stable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Stable
sealed interface AsyncState<out T> {
    data object Loading : AsyncState<Nothing>
    data class Content<T>(val data: T) : AsyncState<T>
    data object Failure : AsyncState<Nothing>

    val isLoading: Boolean
        get() = this is Loading

    val isContent: Boolean
        get() = this is Content
}

@OptIn(ExperimentalContracts::class)
fun <T> AsyncState<T>.isContent(): Boolean {
    contract {
        returns(true) implies (this@isContent is AsyncState.Content<T>)
    }
    return this is AsyncState.Content
}

@OptIn(ExperimentalContracts::class)
fun <T> AsyncState<T>.isLoading(): Boolean {
    contract {
        returns(true) implies (this@isLoading is AsyncState.Loading)
    }
    return this is AsyncState.Loading
}

fun <T> T?.toAsyncState(): AsyncState<T & Any> {
    return when {
        this == null || this is Collection<*> && this.isEmpty() -> AsyncState.Failure

        else -> AsyncState.Content(this)
    }
}
