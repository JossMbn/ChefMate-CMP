package com.jmabilon.chefmate.designsystem.extension

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * Triggers [onLoadMore] when the user scrolls close to the end of the grid.
 *
 * @param buffer Number of items before the end of the list at which [onLoadMore] fires.
 */
@Composable
fun OnReachEnd(
    state: LazyGridState,
    buffer: Int = 2,
    onLoadMore: () -> Unit
) {
    val currentOnLoadMore by rememberUpdatedState(onLoadMore)

    LaunchedEffect(state) {
        snapshotFlow {
            val layoutInfo = state.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            val threshold = (totalItemsCount - buffer).coerceAtLeast(0)

            totalItemsCount > 0 && lastVisibleItemIndex >= threshold
        }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                currentOnLoadMore()
            }
    }
}
