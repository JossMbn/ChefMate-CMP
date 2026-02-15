package com.jmabilon.chefmate.feature.welcome.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.already_have_account
import chefmate.composeapp.generated.resources.get_started
import chefmate.composeapp.generated.resources.sign_in
import com.jmabilon.chefmate.designsystem.component.CMButton
import com.jmabilon.chefmate.designsystem.extension.customClickable
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun WelcomeFooter(
    modifier: Modifier = Modifier,
    onGetStartedButtonClick: () -> Unit,
    onSignInClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CMButton(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.get_started),
            onClick = onGetStartedButtonClick
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.already_have_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                modifier = Modifier
                    .customClickable(
                        rippleEnabled = false,
                        onClick = onSignInClick
                    ),
                text = stringResource(Res.string.sign_in),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
private fun WelcomeFooterPreview() {
    ChefMateTheme {
        WelcomeFooter(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            onGetStartedButtonClick = { /* no-op */ },
            onSignInClick = { /* no-op */ }
        )
    }
}
