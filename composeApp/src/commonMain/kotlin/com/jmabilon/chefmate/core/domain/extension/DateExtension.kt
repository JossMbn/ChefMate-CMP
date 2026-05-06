package com.jmabilon.chefmate.core.domain.extension

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

enum class PresentationDateTimePattern(val value: String) {
    // Input format
    DateTimeVeryLongInputFormatISO("yyyy-MM-dd'T'HH:mm:ss.SSSX"), // ISO means it dynamically parse the Time Zone
    DateTimeVeryLongInputFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"),
    DateTimeInputFormat("yyyy-MM-dd'T'HH:mm:ss"),
    DateTimeShortInputFormat("yyyy-MM-dd"),

    // Date
    DayMonthYearHourMinuteSecondFormat("yyyyMMdd HH:mm:ss"),
    DayMonthYearLittleFormat("dd/MM/yyyy"),
    DayMonthLittleFormat("dd/MM"),
    DayMonthVeryLongFormat("EEEE d MMMM"),
    DayMonthFormat("EEEE d"),
    DaySmallFormat("EEE"),

    // Hour
    SlotHourDisplayFormat("HH:mm"),
    SlotHourDisplayAlternativeFormat("HH'h'mm"),
    SlotHourDisplayAlternativeLuckyCartFormat("HH'H'mm")
}

fun String.toLocalDateTime(): LocalDateTime {
    val instant = Instant.parse(this)
    return instant.toLocalDateTime(TimeZone.currentSystemDefault())
}
