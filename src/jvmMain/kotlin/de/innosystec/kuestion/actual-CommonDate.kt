package de.innosystec.kuestion

import java.time.LocalDateTime

actual typealias CommonDate = LocalDateTime

//TODO review: umbennen, was genau wird gecheckt?
actual fun CommonDateUtil.checkDate(z: CommonDate): Boolean {
    return z > LocalDateTime.now()
}