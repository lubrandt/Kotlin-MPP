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
import java.time.LocalDateTime

internal fun Routing.postSurvey() {
    route("/postSurvey") {
        post {
            val survey = call.receive<SurveyCreation>()
            var hash = ""
            transaction {
                addLogger(StdOutSqlLogger)
                SchemaUtils.create(SurveyTable, AnswerTable)
                hash = createSurveyQuestion(survey.question, Time.now()) //Survey is over NOW?! xD
                survey.answers.forEach {
                    insertAnswer(hash, it)
                }
            }
            call.respond(hash)
        }
    }
}
