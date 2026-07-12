package com.jmabilon.chefmate.feature.overlay.addrecipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_add_rounded_fill
import com.jmabilon.chefmate.core.designsystem.sheet.BottomSheetWithTitleContainer
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.overlay.addrecipe.component.AddRecipeCard
import com.jmabilon.chefmate.feature.overlay.addrecipe.component.HighlightAddRecipeCard
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCreateFromScratch: () -> Unit,
    onScanFromCameraClick: () -> Unit,
    onScanFromUrlClick: () -> Unit,
    onScanFromTextClick: () -> Unit
) {
    BottomSheetWithTitleContainer(
        modifier = modifier,
        label = "Add a recipe",
        onDismissRequest = onDismissRequest
    ) { dismissRequester ->
        AddRecipeBottomSheetContent(
            onCreateFromScratch = {
                onCreateFromScratch()
                dismissRequester()
            },
            onScanFromCameraClick = {
                onScanFromCameraClick()
                dismissRequester()
            },
            onScanFromUrlClick = {
                onScanFromUrlClick()
                dismissRequester()
            },
            onScanFromTextClick = {
                onScanFromTextClick()
                dismissRequester()
            }
        )
    }
}

@OptIn(ExperimentalGridApi::class)
@Composable
private fun AddRecipeBottomSheetContent(
    onCreateFromScratch: () -> Unit,
    onScanFromCameraClick: () -> Unit,
    onScanFromUrlClick: () -> Unit,
    onScanFromTextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HighlightAddRecipeCard(
            modifier = Modifier.fillMaxWidth(),
            title = "From social media",
            description = "Share to ChefMate from social apps",
            onClick = { /* no-op */ }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AddRecipeCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                painter = painterResource(Res.drawable.ic_add_rounded_fill),
                title = "From a photo",
                onClick = onScanFromCameraClick
            )

            AddRecipeCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                painter = painterResource(Res.drawable.ic_add_rounded_fill),
                title = "From a URL",
                onClick = onScanFromUrlClick
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AddRecipeCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                painter = painterResource(Res.drawable.ic_add_rounded_fill),
                title = "From a text",
                onClick = onScanFromTextClick
            )

            AddRecipeCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                painter = painterResource(Res.drawable.ic_add_rounded_fill),
                title = "From scratch",
                onClick = onCreateFromScratch
            )
        }
    }
}

@Preview
@Composable
private fun AddRecipeBottomSheetContentPreview() {
    ChefMateTheme {
        AddRecipeBottomSheetContent(
            onCreateFromScratch = { /* no-op */ },
            onScanFromCameraClick = { /* no-op */ },
            onScanFromUrlClick = { /* no-op */ },
            onScanFromTextClick = { /* no-op */ }
        )
    }
}
