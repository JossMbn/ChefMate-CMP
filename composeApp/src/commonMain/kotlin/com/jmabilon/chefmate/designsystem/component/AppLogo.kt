package com.jmabilon.chefmate.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.app_name
import chefmate.composeapp.generated.resources.ic_local_dining_rounded
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    withAppName: Boolean = false
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraSmall)
                .background(MaterialTheme.colorScheme.primary)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(Res.drawable.ic_local_dining_rounded),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        if (withAppName) {
            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
private fun AppLogoPreview() {
    ChefMateTheme {
        AppLogo(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            withAppName = true
        )
    }
}
