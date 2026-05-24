package com.jmabilon.chefmate.core.designsystem.component.appbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CMTopAppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    navigationIcon: @Composable () -> Unit = { /* no-op */ },
    actions: @Composable (RowScope.() -> Unit) = { /* no-op */ },
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.onBackground,
        navigationIconContentColor = MaterialTheme.colorScheme.onBackground
    )
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            if (!title.isNullOrEmpty()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        colors = colors,
        navigationIcon = navigationIcon,
        actions = actions
    )
}

@Preview
@Composable
private fun CMTopAppBarPreview() {
    ChefMateTheme {
        CMTopAppBar(
            title = "Top App Bar",
            navigationIcon = {
                TopAppBarBackIcon(
                    onClick = { /* no-op */ })
            }
        )
    }
}
