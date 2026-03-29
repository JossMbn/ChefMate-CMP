package com.jmabilon.chefmate.feature.authentication.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_local_dining_rounded_outlined
import com.jmabilon.chefmate.designsystem.component.button.CMButton
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun AuthProviderCompanyButton(
    modifier: Modifier = Modifier,
    label: String,
    leadingIcon: Painter,
    onClick: () -> Unit
) {
    CMButton(
        modifier = modifier.fillMaxWidth(),
        label = label,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        leadingContent = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = leadingIcon,
                contentDescription = null,
                tint = Color.Unspecified
            )
        },
        onClick = onClick
    )
}

@Preview
@Composable
private fun AuthProviderCompanyButtonPreview() {
    ChefMateTheme {
        AuthProviderCompanyButton(
            label = "Continue with Google",
            leadingIcon = painterResource(Res.drawable.ic_local_dining_rounded_outlined),
            onClick = { /* no-op */ }
        )
    }
}
