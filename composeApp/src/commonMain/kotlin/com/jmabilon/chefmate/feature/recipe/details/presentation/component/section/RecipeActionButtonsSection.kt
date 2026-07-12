package com.jmabilon.chefmate.feature.recipe.details.presentation.component.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_bookmark_rounded_outlined
import chefmate.composeapp.generated.resources.ic_edit_rounded_outlined
import chefmate.composeapp.generated.resources.ic_play_arrow_rounded_fill
import com.jmabilon.chefmate.core.designsystem.newcomponent.button.IconButton
import com.jmabilon.chefmate.core.designsystem.newcomponent.button.PrimaryButton
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun RecipeActionButtonsSection(
    modifier: Modifier = Modifier,
    onCookModeClick: () -> Unit,
    onAddToCookbookClick: () -> Unit,
    onEditClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 50.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PrimaryButton(
            label = "Start cooking",
            leadingIcon = painterResource(Res.drawable.ic_play_arrow_rounded_fill),
            onClick = onCookModeClick
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            modifier = Modifier.size(50.dp),
            painter = painterResource(Res.drawable.ic_bookmark_rounded_outlined),
            contentDescription = "Add to a Cookbook",
            onClick = onAddToCookbookClick
        )

        IconButton(
            modifier = Modifier.size(50.dp),
            painter = painterResource(Res.drawable.ic_edit_rounded_outlined),
            contentDescription = "Edit Recipe",
            onClick = onEditClick
        )
    }
}

@Preview
@Composable
private fun RecipeActionButtonsSectionPreview() {
    ChefMateTheme {
        RecipeActionButtonsSection(
            onCookModeClick = { /* no-op */ },
            onAddToCookbookClick = { /* no-op */ },
            onEditClick = { /* no-op */ }
        )
    }
}
