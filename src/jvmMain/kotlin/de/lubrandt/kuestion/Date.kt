package de.lubrandt.kuestion

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal fun createDate(receivedDate: String): LocalDateTime {
    return  LocalDateTime.parse(receivedDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}