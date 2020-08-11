package de.innosystec.kuestion.network

import kotlin.js.Date

actual typealias CommonDate = Date

/**
 * checks if the given date is less than the current date
 */
actual fun CommonDateUtil.compareToCurrentDate(z: CommonDate): Boolean {
    return z.getTime() > Date.now()
}
