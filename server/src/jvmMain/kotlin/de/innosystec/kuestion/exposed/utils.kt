package de.innosystec.kuestion.exposed

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

val stdLogger = StdOutSqlLogger

data class Survey(val question: String, val hash: Int, val expirationTime: LocalDateTime)

fun <T, K : Table> loggedSchemaUtilsTransaction(
    db: Database? = null,
    vararg tables: K,
    statement: Transaction.() -> T
): T =
    transaction(db) {
        SchemaUtils.create(*tables)
        addLogger(stdLogger)
        statement()
    }
