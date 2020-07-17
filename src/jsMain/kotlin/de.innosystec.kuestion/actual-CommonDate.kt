package de.innosystec.kuestion

import kotlin.js.Date

actual typealias CommonDate = Date

actual fun CommonDateUtil.checkDate(z: CommonDate): Boolean {
    return z.getTime() > Date.now()
}

//actual fun CommonDateUtil.now() = Date.now()
actual val number: Int
    get() {
        return 5
    }