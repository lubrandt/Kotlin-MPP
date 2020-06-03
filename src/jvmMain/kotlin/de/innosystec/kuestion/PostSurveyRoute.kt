package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.createSurvey
import de.innosystec.kuestion.exposed.db.AnswerTable
import de.innosystec.kuestion.exposed.db.SurveyTable
import de.innosystec.kuestion.exposed.insertNewAnswer
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.route
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

internal fun Routing.postSurvey() {
    route("/postSurvey") {
        post {
            val survey = call.receive<SurveyCreation>()
            var hash = ""
            transaction {
                SchemaUtils.create(SurveyTable, AnswerTable)
                hash = createSurvey(survey.question, createDate(survey.expirationTime))
//                hash = createSurveyQuestion(survey.question, parseStringToLocalDateTime(LocalDateTime.now().toString()))
                survey.answers.forEach {
                    insertNewAnswer(hash, it)
                }
            }
            call.respond(hash)
        }
    }
}


