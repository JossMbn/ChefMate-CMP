package com.jmabilon.chefmate.feature.recipe.creation.presentation.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.jmabilon.chefmate.core.designsystem.component.textfield.DefaultFieldDecoration
import com.jmabilon.chefmate.core.designsystem.extension.customClickable
import com.jmabilon.chefmate.core.designsystem.newcomponent.common.FieldLabelContainer
import com.jmabilon.chefmate.core.designsystem.sheet.CMTimePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeInputField(
    modifier: Modifier = Modifier,
    hour: Int?,
    minute: Int?,
    onValueChange: (hour: Int, minute: Int) -> Unit,
    label: String? = null,
    hint: String? = null,
    singleLine: Boolean = false,
    leadingContent: @Composable (() -> Unit)? = null,
) {
    var isPickerVisible by remember { mutableStateOf(false) }

    val formattedValue = remember(hour, minute) {
        buildString {
            val hasHour = hour != null && hour > 0

            if (hasHour) {
                val hourLabel = if (hour == 1) "hour" else "hours"
                append("$hour $hourLabel")
            }

            if (minute != null) {
                if (hasHour) append(" ")
                val minuteLabel = if (minute == 1) "min" else "mins"
                append("$minute $minuteLabel")
            }
        }
    }

    val timePickerState = remember(hour, minute) {
        TimePickerState(
            initialHour = hour ?: 0,
            initialMinute = minute ?: 0,
            is24Hour = true
        )
    }

    FieldLabelContainer(
        modifier = modifier,
        label = label
    ) {
        DefaultFieldDecoration(
            modifier = Modifier.customClickable { isPickerVisible = true },
            innerField = {
                Text(
                    modifier = Modifier.basicMarquee(),
                    text = formattedValue,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = if (singleLine) 1 else Int.MAX_VALUE,
                    overflow = TextOverflow.Ellipsis
                )
            },
            value = formattedValue,
            hint = hint,
            leadingContent = leadingContent
        )
    }

    if (isPickerVisible) {
        CMTimePickerDialog(
            timePickerState = timePickerState,
            onConfirmClick = { hour, minute ->
                isPickerVisible = false
                onValueChange(hour, minute)
            },
            onDismissRequest = { isPickerVisible = false }
        )
    }
}
