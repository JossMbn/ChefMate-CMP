package com.jmabilon.chefmate.core.designsystem.component.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.delete
import chefmate.composeapp.generated.resources.ic_more_vert_rounded_fill
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MoreOptionsMenuButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String?,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    options: @Composable ColumnScope.(() -> Unit) -> Unit
) {
    var isContextMenuVisible by rememberSaveable { mutableStateOf(false) }

    Box {
        IconButton(
            modifier = modifier,
            onClick = { isContextMenuVisible = !isContextMenuVisible },
            colors = colors
        ) {
            Icon(
                painter = painter,
                contentDescription = contentDescription
            )
        }

        DropdownMenu(
            modifier = Modifier.width(180.dp),
            expanded = isContextMenuVisible,
            onDismissRequest = { isContextMenuVisible = false },
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            offset = DpOffset(0.dp, 0.dp),
            content = {
                options { isContextMenuVisible = false }
            }
        )
    }
}

@Composable
fun DropdownMenuItemView(
    modifier: Modifier = Modifier,
    menuTitle: String,
    painter: Painter? = null,
    contentDescription: String? = null,
    colors: MenuItemColors = MenuDefaults.itemColors(),
    onClick: () -> Unit
) {
    DropdownMenuItem(
        modifier = modifier,
        text = {
            Text(
                text = menuTitle,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trailingIcon = painter?.let {
            {
                Icon(
                    painter = painter,
                    contentDescription = contentDescription,
                )
            }
        },
        colors = colors,
        onClick = onClick
    )
}

@Preview
@Composable
private fun MoreOptionsMenuButtonPreview() {
    ChefMateTheme {
        MoreOptionsMenuButton(
            painter = painterResource(Res.drawable.ic_more_vert_rounded_fill),
            contentDescription = null,
            options = {
                DropdownMenuItemView(
                    menuTitle = stringResource(Res.string.delete),
                    onClick = {
                        /* no-op */
                    }
                )
            }
        )
    }
}
