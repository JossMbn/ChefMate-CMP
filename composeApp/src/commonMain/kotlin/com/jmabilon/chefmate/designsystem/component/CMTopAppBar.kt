package com.jmabilon.chefmate.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_arrow_back_rounded
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CMTopAppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit) = {},
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
        navigationIcon = {
            if (onNavigationClick != null) {
                IconButton(
                    onClick = onNavigationClick
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_back_rounded),
                        contentDescription = null
                    )
                }
            }
        },
        actions = actions
    )
}

@Preview
@Composable
private fun CMTopAppBarPreview() {
    ChefMateTheme {
        CMTopAppBar(
            title = "Top App Bar",
            onNavigationClick = { /* no-op */ }
        )
    }
}
