package com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation.component.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
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
fun CookbookListEmptyContent(
    innerPadding: PaddingValues,
    onAddCookbookClick: () -> Unit
) {
    val shadowColor = MaterialTheme.colorScheme.primaryContainer

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding + PaddingValues(vertical = 16.dp, horizontal = 50.dp)),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            PreviewCookbookCard(
                modifier = Modifier
                    .offset(x = (-100).dp, y = (-60).dp)
                    .rotate(-10f),
                tint = MaterialTheme.colorScheme.primaryContainer
            )

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

            PreviewCookbookCard(
                modifier = Modifier
                    .offset(x = (100).dp, y = (-30).dp)
                    .rotate(10f),
                tint = Color(0xFFcee0ce)
            )
        }

        Text(
            text = "No cookbooks yet",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Cookbooks help you group recipes by theme — weeknight dinners, holiday baking, whatever you like.",
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

@Composable
private fun PreviewCookbookCard(
    modifier: Modifier = Modifier,
    tint: Color
) {
    Column(
        modifier = modifier
            .widthIn(max = 74.dp)
            .height(92.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
            .padding(6.dp)
            .padding(bottom = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(
                    tint,
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 4.dp
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(6.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.shapes.large
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(6.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.shapes.large
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CookbookListEmptyContentPreview() {
    ChefMateTheme {
        CookbookListEmptyContent(
            innerPadding = PaddingValues(0.dp),
            onAddCookbookClick = { /* no-op */ }
        )
    }
}

@Preview
@Composable
private fun PreviewCookbookCardPreview() {
    ChefMateTheme {
        PreviewCookbookCard(
            modifier = Modifier,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
