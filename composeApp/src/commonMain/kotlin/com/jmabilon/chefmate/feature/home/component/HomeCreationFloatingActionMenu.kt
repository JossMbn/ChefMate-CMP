package com.jmabilon.chefmate.feature.home.component

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_add_notes_rounded_fill
import chefmate.composeapp.generated.resources.ic_add_rounded_outlined
import chefmate.composeapp.generated.resources.ic_collections_bookmark_rounded_fill
import chefmate.composeapp.generated.resources.ic_document_scanner_rounded_fill
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeCreationFloatingActionMenu(
    modifier: Modifier = Modifier,
    onNewCollectionClick: () -> Unit,
    onFromScratchRecipeClick: () -> Unit,
    onScanRecipeClick: () -> Unit,
) {
    var fabExpanded by remember { mutableStateOf(false) }

    FloatingActionButtonMenu(
        modifier = modifier,
        expanded = fabExpanded,
        button = {
            FloatingActionButton(
                onClick = { fabExpanded = !fabExpanded },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_add_rounded_outlined),
                    contentDescription = null
                )
            }
        }
    ) {
        FloatingActionButtonMenuItem(
            onClick = {
                fabExpanded = false
                onNewCollectionClick()
            },
            text = { Text(text = "New Collections") },
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_collections_bookmark_rounded_fill),
                    contentDescription = null
                )
            },
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        )

        FloatingActionButtonMenuItem(
            onClick = {
                fabExpanded = false
                onFromScratchRecipeClick()
            },
            text = { Text(text = "From Scratch Recipe") },
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_add_notes_rounded_fill),
                    contentDescription = null
                )
            },
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        )

        FloatingActionButtonMenuItem(
            onClick = {
                fabExpanded = false
                onScanRecipeClick()
            },
            text = { Text(text = "Scan Recipe") },
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_document_scanner_rounded_fill),
                    contentDescription = null
                )
            },
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        )
    }
}
