package com.jmabilon.chefmate.feature.recipe.creation2.presentation.component.time

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.extension.customClickable
import com.jmabilon.chefmate.core.designsystem.newcomponent.common.FieldLabelContainer
import com.jmabilon.chefmate.core.designsystem.sheet.CMTimePickerDialog
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.creation2.presentation.model.TimeUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeItem(
    modifier: Modifier = Modifier,
    label: String,
    model: TimeUiModel,
    onValueChange: (Int, Int) -> Unit
) {
    var isPickerVisible by remember { mutableStateOf(false) }
    val timePickerState = remember(model.hour, model.minute) {
        TimePickerState(
            initialHour = model.hour,
            initialMinute = model.minute,
            is24Hour = true
        )
    }

    val shape = MaterialTheme.shapes.medium

    FieldLabelContainer(
        modifier = modifier,
        label = label
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 60.dp)
                .clip(shape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = shape
                )
                .background(MaterialTheme.colorScheme.surface)
                .customClickable(onClick = { isPickerVisible = true })
                .padding(vertical = 12.dp, horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = model.time.asStringComposable(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
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

@Preview
@Composable
private fun TimeItemPreview() {
    ChefMateTheme {
        TimeItem(
            label = "Prep Time",
            model = TimeUiModel(
                hour = 0,
                minute = 10
            ),
            onValueChange = { _, _ -> /* no-op */ }
        )
    }
}
