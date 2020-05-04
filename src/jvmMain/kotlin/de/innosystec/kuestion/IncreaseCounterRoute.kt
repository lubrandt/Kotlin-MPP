package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.addAnswerCount
import de.innosystec.kuestion.exposed.db.AnswerTable
import de.innosystec.kuestion.exposed.db.SurveyTable
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

internal fun Routing.counter() {
    route("/inc") {
        post {
            val incAnswer = call.receive<clickedAnswer>()
            addAnswerCount(incAnswer)
            call.respond("true")
        }
    }
}