package com.jmabilon.chefmate.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme

@Composable
fun HomeSectionContainer(
    modifier: Modifier = Modifier,
    sectionName: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = sectionName,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )

        content()
    }
}

@Preview
@Composable
private fun HomeSectionContainerPreview() {
    ChefMateTheme {
        HomeSectionContainer(
            sectionName = "Section Name",
            content = {
                Text(text = "Content")
            }
        )
    }
}
