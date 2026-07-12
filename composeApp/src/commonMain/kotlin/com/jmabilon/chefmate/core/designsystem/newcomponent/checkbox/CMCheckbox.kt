package com.jmabilon.chefmate.core.designsystem.newcomponent.checkbox

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_check_rounded_fill
import com.jmabilon.chefmate.core.designsystem.extension.customClickable
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun CMCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    val uncheckedColor = Color.Transparent
    val uncheckedBorderColor = MaterialTheme.colorScheme.outlineVariant
    val checkedColor = MaterialTheme.colorScheme.primary
    val backgroundColor by animateColorAsState(
        targetValue = if (checked) checkedColor else uncheckedColor
    )
    val borderColor by animateColorAsState(
        targetValue = if (checked) checkedColor else uncheckedBorderColor
    )

    Box(
        modifier = modifier
            .defaultMinSize(26.dp, 26.dp)
            .clip(MaterialTheme.shapes.small)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = MaterialTheme.shapes.small
            )
            .drawBehind { drawRect(color = backgroundColor) }
            .customClickable(onClick = { onCheckedChange(!checked) }),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = checked,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(Res.drawable.ic_check_rounded_fill),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview
@Composable
private fun CMCheckboxPreview() {
    var checked by remember { mutableStateOf(false) }

    ChefMateTheme {
        CMCheckbox(
            checked = checked,
            onCheckedChange = { checked = it }
        )
    }
}
