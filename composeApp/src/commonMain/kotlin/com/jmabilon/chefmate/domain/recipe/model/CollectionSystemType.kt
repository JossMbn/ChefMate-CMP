package com.jmabilon.chefmate.domain.recipe.model

enum class CollectionSystemType {
    Uncategorized,
    Favorites;

    companion object {

        /**
         * Convert a string value from Supabase to a [CollectionSystemType].
         * Supabase uses "FAVORITES" for Favorites and "UNCATEGORIZED" for Uncategorized.
         */
        fun fromValue(value: String?): CollectionSystemType? = when (value?.uppercase()) {
            "FAVORITES" -> Favorites
            "UNCATEGORIZED" -> Uncategorized
            else -> null
        }

        /**
         * Convert the [CollectionSystemType] to a string value for Supabase communication.
         */
        fun toValue(type: CollectionSystemType): String = when (type) {
            Favorites -> "FAVORITES"
            Uncategorized -> "UNCATEGORIZED"
        }
    }
}
