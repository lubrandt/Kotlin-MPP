package de.innosystec.kuestion

import java.time.LocalDateTime

actual typealias Zeit = LocalDateTime

actual fun Zeiten.checkDate(z: Zeit): Boolean {
    return z < LocalDateTime.now()
}