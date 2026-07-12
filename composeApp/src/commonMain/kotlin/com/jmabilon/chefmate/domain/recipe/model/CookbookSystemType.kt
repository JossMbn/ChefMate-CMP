package com.jmabilon.chefmate.domain.recipe.model

enum class CookbookSystemType {
    Uncategorized,
    Favorites;

    companion object {

        /**
         * Convert a string value from Supabase to a [CookbookSystemType].
         * Supabase uses "FAVORITES" for Favorites and "UNCATEGORIZED" for Uncategorized.
         */
        fun fromValue(value: String?): CookbookSystemType? = when (value?.uppercase()) {
            "FAVORITES" -> Favorites
            "UNCATEGORIZED" -> Uncategorized
            else -> null
        }

        /**
         * Convert the [CookbookSystemType] to a string value for Supabase communication.
         */
        fun toValue(type: CookbookSystemType): String = when (type) {
            Favorites -> "FAVORITES"
            Uncategorized -> "UNCATEGORIZED"
        }
    }
}
