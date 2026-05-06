package com.jmabilon.chefmate.core.presentation.extension

/**
 * Rounds the double to one decimal place and formats it as a string.
 * If the rounded value is a whole number, it will be displayed without the decimal point.
 *
 * Examples:
 *  - 1.234 -> "1.2"
 *  - 1.0 -> "1"
 *  - 1.25 -> "1.3"
 */
fun Double.formatQuantity(): String {
    val rounded = (this * 10).toInt() / 10.0
    return if (rounded % 1.0 == 0.0) {
        rounded.toInt().toString()
    } else {
        rounded.toString()
    }
}
