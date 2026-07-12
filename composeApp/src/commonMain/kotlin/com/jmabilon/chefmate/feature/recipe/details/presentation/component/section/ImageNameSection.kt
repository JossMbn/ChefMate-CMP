package com.jmabilon.chefmate.feature.recipe.details.presentation.component.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.component.recipe.RecipeImageWithPlaceHolder
import com.jmabilon.chefmate.core.designsystem.extension.negativePadding
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme

@Composable
fun ImageNameSection(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    recipeName: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        RecipeImageWithPlaceHolder(
            modifier = Modifier
                .negativePadding(horizontal = 16.dp),
            imageModifier = Modifier
                .fillMaxWidth()
                .height(360.dp),
            imageUrl = imageUrl
        )

        Box(
            modifier = Modifier
                .negativePadding(horizontal = 16.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 26.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = recipeName,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
private fun ImageNameSectionPreview() {
    ChefMateTheme {
        ImageNameSection(
            imageUrl = null,
            recipeName = "Delicious cake"
        )
    }
}
