package com.jmabilon.chefmate.core.designsystem.provider.compositionlocal

import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp

/**
 * A composable function that removes the default Material 3 padding for interactive components.
 *
 * This function uses [CompositionLocalProvider] to override the value of [LocalMinimumInteractiveComponentSize]
 * and set it to 0.dp, effectively removing the default padding applied to interactive components.
 *
 * @param content The content to be displayed within this provider.
 */
@Composable
fun RemoveDefaultPaddingProvider(content: @Composable () -> Unit) {
    // CompositionLocalProvider is used to override the value of LocalMinimumInteractiveComponentSize
    // and remove default Material 3 padding.
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides 0.dp,
        content = content
    )
}
