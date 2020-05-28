package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.Answer
import de.innosystec.kuestion.exposed.db.AnswerTable
import de.innosystec.kuestion.exposed.db.SurveyTable
import de.innosystec.kuestion.exposed.getAnswers
import de.innosystec.kuestion.exposed.mapToSurvey
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.route
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

internal fun Routing.getSurvey() {
    route("/{questionId}") {
        get {
            val hash = call.parameters["questionId"]
            val data = SurveyReceiving() //default value for time???
            var exists = 0L
//            println(hash)

//            data.question = "MockQuestion?"

            if (hash != null) {
                if (hash.length != 6) {
//                    dataMock.forEach { data.answers.add(it) }
//                    data.answers.add(ChartSliceData("hashIdTooLongOrTooShort", 5, "#77bcbd"))
                    //todo: fix responses depending on the existance of the survey
                    call.respond(data)
                }
            }

            transaction {
                addLogger(StdOutSqlLogger)
                SchemaUtils.create(SurveyTable, AnswerTable)
                exists = SurveyTable.select { SurveyTable.hash eq hash.toString() }.count()
            }

            if (exists == 0L) {
//                call.respondRedirect("/survey_not_found") // Error Handling in Frontend?
                // what if no results?
//                dataMock.forEach { data.answers.add(it) }
//                data.answers.add(ChartSliceData("notfound", 15, "#006400"))
                call.respond(data)
            } else {
                var answerList: List<Answer> = mutableListOf()
                if (hash != null) {
                    answerList = getAnswers(hash)
                }
                answerList.forEach {
//                    println(it)
//                    val count = it.counts //+ 10
//                    println("added: $it with $count")
                    data.answers.add(ChartSliceData(it.text, it.counts, it.color))
                }
                transaction {
                    data.question = SurveyTable.select{ SurveyTable.hash eq hash.toString()}.map { mapToSurvey(it) }.first().question
                    data.expirationTime = SurveyTable.select{ SurveyTable.hash eq hash.toString()}.map { mapToSurvey(it) }.first().expirationTime.toString()
                    // todo: Beware the Genauigkeitsfehler! 6 statt 9 Stellen aus der Datenbank
//                    println("sendTime: ${data.expirationTime}")
                }
                // uncomment both below to have at least some result
//                dataMock.forEach { data.answers.add(it) }
//                data.answers.add(ChartSliceData("found", 10, "#6224B8"))
                call.respond(data)
            }
        }
        delete {
            val hash = call.parameters["questionId"]
            transaction {
                addLogger(StdOutSqlLogger)
                SchemaUtils.create(SurveyTable, AnswerTable)
                AnswerTable.deleteWhere { AnswerTable.survey eq hash.toString() }
                SurveyTable.deleteWhere { SurveyTable.hash eq hash.toString() }
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}

