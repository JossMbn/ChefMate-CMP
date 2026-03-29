package com.jmabilon.chefmate.designsystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Stable
sealed interface UiText {
    data class DynamicString(val value: String) : UiText

    class ResourceString(
        val resource: StringResource,
        vararg val args: Any
    ) : UiText

    class PluralResourceString(
        val resource: PluralStringResource,
        val count: Int,
        vararg val formatArgs: Any
    ) : UiText

    @Composable
    fun asStringComposable(): String {
        return when (this) {
            is DynamicString -> value
            is ResourceString -> stringResource(resource = resource, formatArgs = args)
            is PluralResourceString -> pluralStringResource(
                resource = resource,
                quantity = count,
                formatArgs = formatArgs
            )
        }
    }

    suspend fun asStringSuspend(): String {
        return when (this) {
            is DynamicString -> value
            is ResourceString -> getString(resource = resource, formatArgs = args)
            is PluralResourceString -> getPluralString(
                resource = resource,
                quantity = count,
                formatArgs = formatArgs
            )
        }
    }

    companion object {
        val Empty = DynamicString("")
    }
}
