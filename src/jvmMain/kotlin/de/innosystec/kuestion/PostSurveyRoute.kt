package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.createSurveyQuestion
import de.innosystec.kuestion.exposed.db.AnswerTable
import de.innosystec.kuestion.exposed.db.SurveyTable
import de.innosystec.kuestion.exposed.insertAnswer
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.route
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Date
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal fun Routing.postSurvey() {
    route("/postSurvey") {
        post {
            val survey = call.receive<SurveyCreation>()
            var hash = ""
            transaction {
                SchemaUtils.create(SurveyTable, AnswerTable)
                hash = createSurveyQuestion(survey.question, createDate(survey.expirationTime))
//                hash = createSurveyQuestion(survey.question, parseStringToLocalDateTime(LocalDateTime.now().toString()))
                survey.answers.forEach {
                    insertAnswer(hash, it)
                }
            }
            call.respond(hash)
        }
    }
}


