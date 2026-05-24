package com.jmabilon.chefmate.feature.home.presentation.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.extension.customClickable
import com.jmabilon.chefmate.core.designsystem.sheet.BottomSheetContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanRecipeBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onScanFromImageClick: () -> Unit,
    onScanFromCameraClick: () -> Unit,
    onScanFromTextClick: () -> Unit,
    onScanFromUrlClick: () -> Unit
) {
    BottomSheetContainer(
        modifier = modifier,
        onDismissRequest = onDismissRequest
    ) { dismissRequester ->
        ScanRecipeBottomSheetContent(
            onDismissRequest = dismissRequester,
            onScanFromImageClick = onScanFromImageClick,
            onScanFromCameraClick = onScanFromCameraClick,
            onScanFromTextClick = onScanFromTextClick,
            onScanFromUrlClick = onScanFromUrlClick
        )
    }
}

@Composable
fun ScanRecipeBottomSheetContent(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onScanFromImageClick: () -> Unit,
    onScanFromCameraClick: () -> Unit,
    onScanFromTextClick: () -> Unit,
    onScanFromUrlClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Scan recipe",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 2,
            maxLines = 2
        ) {
            ScanItem(
                modifier = Modifier.weight(1f),
                title = "From image",
                onClick = {
                    onScanFromImageClick()
                    onDismissRequest()
                }
            )

            ScanItem(
                modifier = Modifier.weight(1f),
                title = "From camera",
                onClick = {
                    onScanFromCameraClick()
                    onDismissRequest()
                }
            )

            ScanItem(
                modifier = Modifier.weight(1f),
                title = "From url",
                onClick = {
                    onScanFromUrlClick()
                    onDismissRequest()
                }
            )

            ScanItem(
                modifier = Modifier.weight(1f),
                title = "Past text",
                onClick = {
                    onScanFromTextClick()
                    onDismissRequest()
                }
            )
        }
    }
}

@Composable
fun ScanItem(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(80.dp)
            .clip(MaterialTheme.shapes.medium)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .customClickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
