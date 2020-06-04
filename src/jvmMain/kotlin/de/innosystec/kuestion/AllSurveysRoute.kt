package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.db.SurveyTable
import de.innosystec.kuestion.exposed.mapToSurvey
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

internal  fun Routing.allSurveys() {
    route("/allSurveys") {
        get {
            val listOfSurveys = mutableListOf<StringPair>()
            transaction {
                addLogger(StdOutSqlLogger)
                SchemaUtils.create(SurveyTable)
                SurveyTable.selectAll()
                    .map { mapToSurvey(it) }
                    .forEach { listOfSurveys.add(StringPair(it.question, it.hash)) }
            }
            call.respond(listOfSurveys)
        }
    }
}
