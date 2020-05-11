package de.innosystec.kuestion

import java.util.Date
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


internal fun createDate(receivedDate: String): LocalDateTime {
    val parsedTime = LocalDateTime.parse(receivedDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    println("createdDateTime: $parsedTime")
    return parsedTime
}