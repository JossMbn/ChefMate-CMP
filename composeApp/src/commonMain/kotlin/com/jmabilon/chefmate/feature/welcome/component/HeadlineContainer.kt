package com.jmabilon.chefmate.feature.welcome.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.welcome_page_description
import chefmate.composeapp.generated.resources.welcome_page_title
import com.jmabilon.chefmate.designsystem.component.AppLogo
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun HeadlineContainer(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppLogo(withAppName = true)

        Text(
            text = stringResource(Res.string.welcome_page_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(Res.string.welcome_page_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun HeadlineContainerPreview() {
    ChefMateTheme {
        HeadlineContainer(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
        )
    }
}
