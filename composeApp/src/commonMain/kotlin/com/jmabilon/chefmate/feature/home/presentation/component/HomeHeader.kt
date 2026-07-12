package com.jmabilon.chefmate.feature.home.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.app_name
import chefmate.composeapp.generated.resources.ic_settings_rounded_outlined
import com.jmabilon.chefmate.core.designsystem.newcomponent.button.IconButton
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {
            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        actions = {
            IconButton(
                painter = painterResource(Res.drawable.ic_settings_rounded_outlined),
                contentDescription = "Navigate to settings screen",
                onClick = onSettingsClick
            )
        },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        contentPadding = PaddingValues(top = 4.dp, bottom = 4.dp, start = 10.dp, end = 20.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeHeaderPreview() {
    ChefMateTheme {
        HomeHeader(
            onSettingsClick = { /* no-op */ }
        )
    }
}
