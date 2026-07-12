package com.jmabilon.chefmate.core.designsystem.newcomponent.appbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_arrow_back_rounded_outlined
import com.jmabilon.chefmate.core.designsystem.newcomponent.button.IconButton
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CMTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    colors: TopAppBarColors = topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.onBackground
    ),
    onNavigationIconClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(
                painter = painterResource(Res.drawable.ic_arrow_back_rounded_outlined),
                contentDescription = "Navigate back",
                onClick = onNavigationIconClick
            )
        },
        colors = colors,
        actions = actions,
        contentPadding = PaddingValues(horizontal = 12.dp)
    )
}

@Preview
@Composable
private fun CMTopAppBarPreview() {
    ChefMateTheme {
        CMTopAppBar(
            title = "Top App Bar",
            onNavigationIconClick = { /* no-op */ }
        )
    }
}
