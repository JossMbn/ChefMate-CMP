package com.jmabilon.chefmate.core.presentation.extension

import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.time_format_hours_minutes
import chefmate.composeapp.generated.resources.time_format_hours_only
import chefmate.composeapp.generated.resources.time_format_minutes_only
import com.jmabilon.chefmate.designsystem.utils.UiText

/**
 * Converts an integer representing seconds into a pair of hours and minutes.
 *
 * @return A [Pair] where the first element is the number of hours and the second element is the number of remaining minutes.
 *         Returns `null` if the input is zero or negative.
 */
fun Int.toHourMinute(): Pair<Int, Int>? {
    val second = this

    if (second <= 0) return null

    val minutes = second / 60
    val hours = minutes / 60
    val remainingMinute = minutes % 60

    return hours to remainingMinute
}

/**
 * Formats an integer representing seconds into a human-readable duration string.
 *
 * @return A [UiText] containing the formatted duration string, or `null` if the input is zero or negative.
 */
fun Int.formatDuration(): UiText? {
    val second = this

    if (this <= 0) return null
    val (hours, minutes) = second.toHourMinute() ?: return null

    return when {
        hours > 0 && minutes > 0 -> {
            UiText.ResourceString(Res.string.time_format_hours_minutes, hours, minutes)
        }

        hours > 0 && minutes == 0 -> {
            UiText.ResourceString(Res.string.time_format_hours_only, hours)
        }

        else -> {
            UiText.ResourceString(Res.string.time_format_minutes_only, minutes)
        }
    }
}
