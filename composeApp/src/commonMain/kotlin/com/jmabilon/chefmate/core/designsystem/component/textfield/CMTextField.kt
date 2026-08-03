package com.jmabilon.chefmate.core.designsystem.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.newcomponent.common.FieldLabelContainer
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme

@Composable
fun CMTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    hint: String? = null,
    singleLine: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    decorationBox: @Composable ((innerTextField: @Composable () -> Unit) -> Unit)? = null
) {
    FieldLabelContainer(
        modifier = modifier,
        label = label
    ) {
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = decorationBox ?: { innerTextField ->
                DefaultFieldDecoration(
                    innerField = innerTextField,
                    value = value,
                    hint = hint,
                    leadingContent = leadingContent,
                    trailingContent = trailingContent
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
