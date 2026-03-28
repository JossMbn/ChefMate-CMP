package com.jmabilon.chefmate.designsystem.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jmabilon.chefmate.designsystem.component.button.CMButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CMTimePickerDialog(
    modifier: Modifier = Modifier,
    timePickerState: TimePickerState = remember {
        TimePickerState(
            initialHour = 0,
            initialMinute = 0,
            is24Hour = true
        )
    },
    onConfirmClick: (Int, Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties()
    ) {
        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimeInput(state = timePickerState)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onDismissRequest() }
                    ) {
                        Text("Dismiss")
                    }

                    CMButton(
                        modifier = Modifier.weight(1f),
                        label = "Confirm",
                        onClick = {
                            onConfirmClick(
                                timePickerState.hour,
                                timePickerState.minute
                            )
                            onDismissRequest()
                        }
                    )
                }
            }
        }
    }

    /*TimePickerDialog(
        modifier = modifier,
        title = { Text("Fake Title") },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmClick(
                        timePickerState.hour,
                        timePickerState.minute
                    )
                    onDismissRequest()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text("Dismiss")
            }
        },
        content = {
            TimePicker(
                state = timePickerState
            )
        }
    )*/
}
