package com.jmabilon.chefmate.core.domain.extension

/**
 * Maps a [Result] of any type to a [Result] of [Unit],
 * effectively discarding the success value while preserving the error if it exists.
 */
fun <T> Result<T>.asEmptyResult(): Result<Unit> {
    return map { /* no-op */ }
}
