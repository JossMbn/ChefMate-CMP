package com.jmabilon.chefmate.domain.recipe.model

enum class TemperatureUnit {
    Celsius,
    Fahrenheit;

    companion object {

        /**
         * Convert a string value from Supabase to a [TemperatureUnit].
         */
        fun fromValue(value: String?): TemperatureUnit = when (value?.uppercase()) {
            "C" -> Celsius
            "F" -> Fahrenheit
            else -> Celsius // Default to Celsius if the value is null or unrecognized
        }

        /**
         * Convert a [TemperatureUnit] to a string value for Supabase communication.
         */
        fun toValue(unit: TemperatureUnit?): String? {
            if (unit == null) return null

            return when (unit) {
                Celsius -> "C"
                Fahrenheit -> "F"
            }
        }
    }
}
