package com.jmabilon.chefmate.feature.cookbook.details.presentation.component.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.extension.shimmerEffect
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.core.presentation.extension.plus

@Composable
fun CookbookDetailsLoadingContent(
    innerPadding: PaddingValues
) {
    val contentPadding = remember(innerPadding) {
        innerPadding + PaddingValues(16.dp)
    }

    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        state = gridState,
        columns = GridCells.Fixed(2),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(6) {
            CookbookDetailsLoadingItem()
        }
    }
}

@Composable
private fun CookbookDetailsLoadingItem(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .widthIn(min = 160.dp, max = 200.dp)
            .heightIn(max = 250.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(MaterialTheme.shapes.large)
                .shimmerEffect()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(12.dp)
                .clip(MaterialTheme.shapes.large)
                .shimmerEffect()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(12.dp)
                .clip(MaterialTheme.shapes.large)
                .shimmerEffect()
        )
    }
}

@Preview
@Composable
private fun CookbookDetailsLoadingContentPreview() {
    ChefMateTheme {
        CookbookDetailsLoadingContent(
            innerPadding = PaddingValues(0.dp)
        )
    }
}
