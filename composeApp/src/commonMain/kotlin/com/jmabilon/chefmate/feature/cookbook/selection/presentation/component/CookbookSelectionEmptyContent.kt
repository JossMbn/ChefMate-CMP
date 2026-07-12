package com.jmabilon.chefmate.feature.cookbook.selection.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_book_rounded_outlined
import com.jmabilon.chefmate.core.designsystem.extension.dashedBorder
import com.jmabilon.chefmate.core.designsystem.newcomponent.button.PrimaryButton
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.core.presentation.extension.plus
import org.jetbrains.compose.resources.painterResource

@Composable
fun CookbookSelectionEmptyContent(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    onAddCookbookClick: () -> Unit
) {
    val contentPadding = remember(innerPadding) {
        innerPadding + PaddingValues(vertical = 16.dp, horizontal = 50.dp)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.large)
                .dashedBorder(
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.large
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(Res.drawable.ic_book_rounded_outlined),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        }

        Text(
            text = "No cookbooks yet",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Cookbooks keep your recipes grouped — by week, by season, or by whoever you cook for.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        PrimaryButton(
            label = "Create a cookbook",
            onClick = onAddCookbookClick
        )
    }
}

@Preview
@Composable
private fun CookbookSelectionEmptyContentPreview() {
    ChefMateTheme {
        CookbookSelectionEmptyContent(
            innerPadding = PaddingValues(0.dp),
            onAddCookbookClick = { /* no-op */ }
        )
    }
}
