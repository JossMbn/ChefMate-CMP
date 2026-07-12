package com.jmabilon.chefmate.feature.cookbook.selection.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.core.presentation.extension.plus
import com.jmabilon.chefmate.feature.cookbook.selection.presentation.component.item.CookbookSelectionItemLoading

@Composable
fun CookbookSelectionLoadingContent(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues
) {
    val contentPadding = remember(innerPadding) {
        innerPadding + PaddingValues(vertical = 16.dp, horizontal = 22.dp)
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(12) {
            CookbookSelectionItemLoading()
        }
    }
}

@Preview
@Composable
private fun CookbookSelectionLoadingContentPreview() {
    ChefMateTheme {
        CookbookSelectionLoadingContent(innerPadding = PaddingValues(0.dp))
    }
}
