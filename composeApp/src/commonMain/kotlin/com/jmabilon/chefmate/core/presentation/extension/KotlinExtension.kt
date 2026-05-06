package com.jmabilon.chefmate.core.presentation.extension

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection

/**
 * Extension function to add two [PaddingValues] together.
 *
 * This function creates a new [PaddingValues] instance that calculates the sum of the top, bottom,
 * left, and right padding from both [PaddingValues] instances. It takes into account the layout
 * direction for calculating left and right padding.
 *
 * @receiver The first [PaddingValues] instance.
 * @param other The second [PaddingValues] instance to be added to the first one.
 * @return A new [PaddingValues] instance that represents the combined padding of both instances.
 */
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    return object : PaddingValues {
        override fun calculateTopPadding() = this@plus.calculateTopPadding() + other.calculateTopPadding()

        override fun calculateBottomPadding() =
            this@plus.calculateBottomPadding() + other.calculateBottomPadding()

        override fun calculateLeftPadding(layoutDirection: LayoutDirection) =
            this@plus.calculateStartPadding(layoutDirection) + other.calculateStartPadding(layoutDirection)

        override fun calculateRightPadding(layoutDirection: LayoutDirection) =
            this@plus.calculateEndPadding(layoutDirection) + other.calculateEndPadding(layoutDirection)
    }
}
