package de.innosystec.kuestion.exposed

import de.innosystec.kuestion.exposed.db.AnswerTable
import de.innosystec.kuestion.exposed.db.SurveyTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

data class Survey(val question: String, val hashCode: Int, val expirationTime: LocalDateTime)

data class Answer(val surveyHashCode: Int, val text: String, val counts: Int)

fun insertAnswer(survey: Survey, answer: Answer): Boolean {
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(AnswerTable,SurveyTable)
        // what if new survey? assume there is one?
    }
    return true
}

fun getAnswers(survey: Survey): List<Answer> {
    var retval: List<Answer> = mutableListOf()
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(AnswerTable,SurveyTable)
        retval = AnswerTable.select {AnswerTable.survey eq survey.hashCode}.map { mapAnswer(it) }
    }
    return retval
}

fun mapSurvey(it: ResultRow) = Survey(
    question = it[SurveyTable.question],
    hashCode = it[SurveyTable.hash],
    expirationTime = it[SurveyTable.expirationTime]
)

fun mapAnswer(it: ResultRow) = Answer(
    surveyHashCode = it[AnswerTable.survey],
    text = it[AnswerTable.text],
    counts = it[AnswerTable.counts]
)