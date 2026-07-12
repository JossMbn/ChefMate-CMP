package com.jmabilon.chefmate.feature.cookbook.selection.presentation.component.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.extension.shimmerEffect
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme

@Composable
fun CookbookSelectionItemLoading(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(MaterialTheme.shapes.medium)
                .shimmerEffect()
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 160.dp, height = 16.dp)
                    .clip(MaterialTheme.shapes.small)
                    .shimmerEffect()
            )

            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 10.dp)
                    .clip(MaterialTheme.shapes.small)
                    .shimmerEffect()
            )
        }

        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(MaterialTheme.shapes.small)
                .shimmerEffect()
        )
    }
}

@Preview
@Composable
private fun CookbookSelectionItemLoadingPreview() {
    ChefMateTheme {
        CookbookSelectionItemLoading()
    }
}
