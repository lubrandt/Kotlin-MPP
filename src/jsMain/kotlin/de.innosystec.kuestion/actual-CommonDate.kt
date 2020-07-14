package de.innosystec.kuestion

import kotlin.js.Date

actual typealias CommonDate = Date

actual fun CommonDateUtil.checkDate(z: CommonDate): Boolean {
    return z.getTime() > Date.now()
}