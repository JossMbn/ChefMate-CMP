package com.jmabilon.chefmate.feature.home.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_add_rounded_fill
import chefmate.composeapp.generated.resources.ic_book_rounded_outlined
import chefmate.composeapp.generated.resources.ic_favorite_rounded_fill
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeQuickAccess(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit,
    onCookbooksClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HomeQuickActionCard(
            modifier = Modifier.weight(1f),
            label = "Ajouter",
            painter = painterResource(Res.drawable.ic_add_rounded_fill),
            painterTint = MaterialTheme.colorScheme.onPrimary,
            cardContainerColor = MaterialTheme.colorScheme.primary,
            onClick = onAddClick
        )

        HomeQuickActionCard(
            modifier = Modifier.weight(1f),
            label = "Cookbooks",
            painter = painterResource(Res.drawable.ic_book_rounded_outlined),
            onClick = onCookbooksClick
        )

        HomeQuickActionCard(
            modifier = Modifier.weight(1f),
            label = "Favorites",
            painter = painterResource(Res.drawable.ic_favorite_rounded_fill),
            painterTint = MaterialTheme.colorScheme.primary,
            onClick = onFavoritesClick
        )
    }
}

@Preview
@Composable
private fun HomeQuickAccessPreview() {
    ChefMateTheme {
        HomeQuickAccess(
            onAddClick = { /* no-op */ },
            onCookbooksClick = { /* no-op */ },
            onFavoritesClick = { /* no-op */ }
        )
    }
}
