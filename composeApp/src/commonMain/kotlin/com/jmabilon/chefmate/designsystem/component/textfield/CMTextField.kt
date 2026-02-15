package com.jmabilon.chefmate.designsystem.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme

@Composable
fun CMTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    hint: String? = null,
    singleLine: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (!label.isNullOrEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                DefaultTextFieldDecoration(
                    innerTextField = innerTextField,
                    value = value,
                    hint = hint
                )
            }
        )
    }
}

@Preview
@Composable
private fun CMTextFieldPreview() {
    ChefMateTheme {
        CMTextField(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            value = "",
            onValueChange = { /* no-op */ },
            label = "Email address",
            hint = "Enter text here"
        )
    }
}
