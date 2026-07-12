package com.jmabilon.chefmate.core.designsystem.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_close_rounded_outlined
import com.jmabilon.chefmate.core.designsystem.newcomponent.button.IconButton
import com.jmabilon.chefmate.core.designsystem.theme.bottomSheetShape
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContainer(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onDismissRequest: () -> Unit,
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    content: @Composable (() -> Unit) -> Unit
) {
    val scope = rememberCoroutineScope()

    val dismissRequester: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissRequest()
            }
        }
    }

    ModalBottomSheet(
        modifier = modifier
            .widthIn(max = 700.dp)
            .heightIn(min = 250.dp),
        onDismissRequest = dismissRequester,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        shape = MaterialTheme.shapes.bottomSheetShape,
        properties = properties,
        dragHandle = dragHandle
    ) {
        content(dismissRequester)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetWithTitleContainer(
    modifier: Modifier = Modifier,
    label: String,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onDismissRequest: () -> Unit,
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    content: @Composable (() -> Unit) -> Unit
) {
    BottomSheetContainer(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        properties = properties,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .width(42.dp)
                    .height(6.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(Color(0xFFD6C6AE))
            )
        }
    ) { dismissRequester ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 22.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                IconButton(
                    painter = painterResource(Res.drawable.ic_close_rounded_outlined),
                    contentDescription = "Close bottom sheet",
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    onClick = dismissRequester
                )
            }

            content(dismissRequester)
        }
    }
}
