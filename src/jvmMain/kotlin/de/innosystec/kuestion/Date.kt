package de.innosystec.kuestion

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal fun parseStringToLocalDateTime(dateTime: String): LocalDateTime {
    if (dateTime == "") throw  IllegalArgumentException("date was empty")
    val parsedTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    println("parsedTime: $parsedTime")
    return parsedTime
}