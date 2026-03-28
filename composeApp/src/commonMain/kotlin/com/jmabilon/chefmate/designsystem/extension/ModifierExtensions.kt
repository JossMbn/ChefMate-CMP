package com.jmabilon.chefmate.designsystem.extension

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

// =================================================================================================
// Clickable
// =================================================================================================

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

// =================================================================================================
// Border
// =================================================================================================

/**
 * A custom modifier to draw a dashed border around a composable.
 *
 * @param color The color of the dashed border.
 * @param shape The shape of the border (e.g., RectangleShape, CircleShape).
 * @param strokeWidth The width of the border stroke. Default is 2.dp.
 * @param dashLength The length of each dash in the border. Default is 4.dp.
 * @param gapLength The length of the gap between dashes. Default is 4.dp.
 * @param cap The style of the stroke cap (e.g., Round, Square). Default is Round.
 */
fun Modifier.dashedBorder(
    color: Color,
    shape: Shape,
    strokeWidth: Dp = 2.dp,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 4.dp,
    cap: StrokeCap = StrokeCap.Round
) = composed {
    this then drawWithContent {
        val outline = shape.createOutline(size, layoutDirection, density = this)
        val dashedStroke = Stroke(
            cap = cap,
            width = strokeWidth.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx())
            )
        )

        // Draw the content
        drawContent()

        // Draw the border
        drawOutline(
            outline = outline,
            style = dashedStroke,
            brush = SolidColor(color)
        )
    }
}

// =================================================================================================
// Padding
// =================================================================================================

/**
 * A custom modifier that allows for negative padding, effectively increasing the size of the composable
 * beyond its measured size. This can be useful for creating overlapping effects or adjusting layout without
 * changing the actual content size.
 *
 * @param start The negative padding to apply on the start side (left in LTR, right in RTL). Default is 0.dp.
 * @param top The negative padding to apply on the top side. Default is 0.dp.
 * @param end The negative padding to apply on the end side (right in LTR, left in RTL). Default is 0.dp.
 * @param bottom The negative padding to apply on the bottom side. Default is 0.dp.
 */
fun Modifier.negativePadding(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp
): Modifier = composed {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    val startPx = with(density) { start.roundToPx() }
    val topPx = with(density) { top.roundToPx() }
    val endPx = with(density) { end.roundToPx() }
    val bottomPx = with(density) { bottom.roundToPx() }

    layout { measurable, constraints ->
        val horizontalExtra = startPx + endPx
        val verticalExtra = topPx + bottomPx

        val maxWidth = if (constraints.hasBoundedWidth) {
            constraints.maxWidth + horizontalExtra
        } else {
            constraints.maxWidth
        }
        val maxHeight = if (constraints.hasBoundedHeight) {
            constraints.maxHeight + verticalExtra
        } else {
            constraints.maxHeight
        }

        val newConstraints = constraints.copy(
            minWidth = (constraints.minWidth + horizontalExtra).coerceAtLeast(0),
            maxWidth = maxWidth,
            minHeight = (constraints.minHeight + verticalExtra).coerceAtLeast(0),
            maxHeight = maxHeight
        )

        val placeable = measurable.measure(newConstraints)

        val width = constraints.maxWidth
            .takeIf { constraints.hasBoundedWidth }
            ?: (placeable.width - horizontalExtra)

        val height = constraints.maxHeight
            .takeIf { constraints.hasBoundedHeight }
            ?: (placeable.height - verticalExtra)

        layout(width, height) {
            val xOffset = when (layoutDirection) {
                LayoutDirection.Ltr -> -startPx
                LayoutDirection.Rtl -> -endPx
            }

            placeable.placeRelative(
                x = xOffset,
                y = -topPx
            )
        }
    }
}

/**
 * A convenience function for applying uniform negative padding on all sides.
 *
 * @param all The negative padding to apply on all sides (start, top, end, bottom).
 */
fun Modifier.negativePadding(all: Dp) = negativePadding(all, all, all, all)

/**
 * A convenience function for applying negative padding symmetrically on horizontal and vertical axes.
 *
 * @param horizontal The negative padding to apply on the start and end sides. Default is 0.dp.
 * @param vertical The negative padding to apply on the top and bottom sides. Default is 0.dp.
 */
fun Modifier.negativePadding(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) = negativePadding(
    start = horizontal,
    top = vertical,
    end = horizontal,
    bottom = vertical
)

// =================================================================================================
// Conditional
// =================================================================================================

/**
 * Applies [ifTrue] modifiers when [condition] is true, and optionally [ifFalse] modifiers otherwise.
 *
 * Using `inline` avoids lambda object allocation on every recomposition, making this recomposition-free.
 *
 * Usage:
 * ```
 * Modifier.conditional(isSelected) { background(Color.Blue) }
 * Modifier.conditional(isEnabled, ifTrue = { alpha(1f) }, ifFalse = { alpha(0.4f) })
 * ```
 */
inline fun Modifier.conditional(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: Modifier.() -> Modifier = { this }
): Modifier = if (condition) ifTrue() else ifFalse()

/**
 * Applies [modifier] only when [value] is not null, passing it as a parameter.
 *
 * Useful to conditionally apply a modifier that depends on a nullable value, without requiring
 * manual null-checks at the call site.
 *
 * Usage:
 * ```
 * Modifier.ifNotNull(selectedColor) { color -> background(color) }
 * ```
 */
inline fun <T : Any> Modifier.ifNotNull(
    value: T?,
    modifier: Modifier.(T) -> Modifier
): Modifier = if (value != null) modifier(value) else this
