package de.lubrandt.kuestion.exposed

import org.jetbrains.exposed.sql.Database

object DatabaseSettings {
    val db by lazy { Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver") }
}