package de.innosystec.kuestion

import kotlin.js.Date

actual typealias Zeit = Date

actual fun Zeiten.checkDate(z: Zeit): Boolean {
    return z.getTime() < Date.now()
}