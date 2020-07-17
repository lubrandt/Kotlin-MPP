package de.innosystec.kuestion

import java.time.LocalDateTime

actual typealias CommonDate = LocalDateTime

actual fun CommonDateUtil.checkDate(z: CommonDate): Boolean {
    return z > LocalDateTime.now()
}

//actual fun CommonDateUtil.now(): LocalDateTime =  LocalDateTime.now()
actual val number: Int
    get() {
        return 5
    }