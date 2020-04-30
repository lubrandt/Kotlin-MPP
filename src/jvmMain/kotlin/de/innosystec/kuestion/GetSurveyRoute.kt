package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.Answer
import de.innosystec.kuestion.exposed.db.AnswerTable
import de.innosystec.kuestion.exposed.db.SurveyTable
import de.innosystec.kuestion.exposed.getAnswers
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

internal fun Routing.getSurvey() {
    route("/{questionId}") {
        get {
            val hash = call.parameters["questionId"]
            val data = SurveyReceiving()
            var exists = 0L

            data.question = "MockQuestion?"

            if (hash != null) {
                if (hash.length != 6) {
                    dataMock.forEach { data.answers.add(it) }
                    data.answers.add(ChartSliceData("hashIdTooLongOrTooShort", 5, "#77bcbd"))
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
                dataMock.forEach { data.answers.add(it) }
                data.answers.add(ChartSliceData("notfound", 15, "#006400"))
                call.respond(data)
            } else {
                var answerList: List<Answer> = mutableListOf()
                transaction {
                    addLogger(StdOutSqlLogger)
                    SchemaUtils.create(SurveyTable, AnswerTable)
                    if (hash != null) {
                        answerList = getAnswers(hash)
                    }
                }
                answerList.forEach {
                    val count = it.counts +10
                    println("added: $it with $count")
                    data.answers.add(ChartSliceData(it.text, count, randHexColor()))
                }
                // uncomment both below to have at least some result
                dataMock.forEach { data.answers.add(it) }
                data.answers.add(ChartSliceData("found", 10, "#6224B8"))
                call.respond(data)
            }
        }
    }
}

internal fun randHexColor(): String {
    val stringLength = 6
    val charPool: List<Char> = ('a'..'f') + ('0'..'9')
    val randomString =
        (1..stringLength)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map { x -> charPool[x] }
            .joinToString("")
    return "#${randomString}"
}