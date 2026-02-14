package com.jmabilon.chefmate.domain.recipe.model

enum class RecipeDifficulty {
    Easy,
    Medium,
    Hard;

    companion object {

        /**
         * Convert an integer value from Supabase to a [RecipeDifficulty].
         * Supabase uses 1 for Easy, 2 for Medium, and 3 for Hard.
         */
        fun fromValue(value: Int?): RecipeDifficulty? =
            entries.find { it.ordinal + 1 == value }

        /**
         * Convert the [RecipeDifficulty] to an integer value for Supabase communication.
         */
        fun toValue(difficulty: RecipeDifficulty?): Int? = difficulty?.ordinal?.plus(1)
    }
}
