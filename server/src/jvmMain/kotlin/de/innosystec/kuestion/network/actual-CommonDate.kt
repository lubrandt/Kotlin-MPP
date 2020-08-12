package de.innosystec.kuestion.network

import java.time.LocalDateTime

actual typealias CommonDate = LocalDateTime

/**
 * checks if the given date is less than the current date
 */
actual fun CommonDateUtil.compareToCurrentDate(z: CommonDate): Boolean {
    return z > LocalDateTime.now()
}
