package com.jmabilon.chefmate.designsystem.extension

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role

/**
 * A custom clickable modifier that allows for optional ripple effect and better control over interaction sources.
 *
 * @param enabled Whether the clickable is enabled or not.
 * @param onClickLabel An optional semantic label for accessibility services.
 * @param role An optional role for accessibility services.
 * @param interactionSource An optional [MutableInteractionSource] to track interactions. If null, a new one will be created.
 * @param rippleEnabled Whether to show a ripple effect on click. Defaults to true.
 * @param onClick The lambda to execute when the element is clicked.
 */
fun Modifier.customClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null, // semantic / accessibility label for the onClick action
    role: Role? = null,
    interactionSource: MutableInteractionSource? = null,
    rippleEnabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val actualInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val indication = remember { if (rippleEnabled) ripple() else null }

    clickable(
        indication = indication,
        interactionSource = actualInteractionSource,
        enabled = enabled,
        onClick = onClick,
        onClickLabel = onClickLabel,
        role = role
    )
}
