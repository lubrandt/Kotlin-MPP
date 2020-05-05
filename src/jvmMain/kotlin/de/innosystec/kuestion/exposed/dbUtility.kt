package de.innosystec.kuestion.exposed

import de.innosystec.kuestion.*
import de.innosystec.kuestion.exposed.db.AnswerTable
import de.innosystec.kuestion.exposed.db.SurveyTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import kotlin.random.Random

data class Survey(val question: String, val hash: String, val expirationTime: LocalDateTime)

data class Answer(val surveyHashCode: String, val text: String, val counts: Int = 0, val color: String)

fun createSurveyQuestion(question: String, expirationTime: LocalDateTime): String {
    val tmpHash = createHash()
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(SurveyTable)
        SurveyTable.insert {
            it[SurveyTable.question] = question
            it[hash] = tmpHash
            it[SurveyTable.expirationTime] = expirationTime
        }
    }
    return tmpHash
}

fun createHash(): String {
    /**
     * not really a hash
     * should be a 6 character string to use with the link and ktor(questionId)
     * based on: https://www.baeldung.com/kotlin-random-alphanumeric-string
     * TODO: created hash not checked for unique but in db is unqiueindex, exception handling?
     */
    val stringLength = 6
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..stringLength)
        .map { Random.nextInt(0, charPool.size) }
        .map { x -> charPool[x] }
        .joinToString("")
}

fun insertAnswer(tmpSurvey: String, tmpAnswer: String) {
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(AnswerTable)
        AnswerTable.insert {
            it[survey] = tmpSurvey
            it[text] = tmpAnswer
            it[color] = randHexColor()
        }
    }
}

fun addAnswerCount(answer: ClickedAnswer) {
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(AnswerTable)
        AnswerTable.update({ (AnswerTable.survey eq answer.surveyHash) and (AnswerTable.text eq answer.answer) }) {
            with(SqlExpressionBuilder) {
                it[counts] = counts + 1
            }
        }
    }
}

// fulltext search for survey Name?
fun getAnswers(hash: String): List<Answer> {
    var retval: List<Answer> = mutableListOf()
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(AnswerTable, SurveyTable)
        retval = AnswerTable.select { AnswerTable.survey eq hash }.map { mapAnswer(it) }
    }
    return retval
}

fun mapSurvey(it: ResultRow) = Survey(
    question = it[SurveyTable.question],
    hash = it[SurveyTable.hash],
    expirationTime = it[SurveyTable.expirationTime]
)

fun mapAnswer(it: ResultRow) = Answer(
    surveyHashCode = it[AnswerTable.survey],
    text = it[AnswerTable.text],
    counts = it[AnswerTable.counts],
    color = it[AnswerTable.color]
)

internal fun randHexColor(): String {
    val stringLength = 6
    val charPool: List<Char> = ('a'..'f') + ('0'..'9')
    val randomString =
        (1..stringLength)
            .map { Random.nextInt(0, charPool.size) }
            .map { x -> charPool[x] }
            .joinToString("")
    return "#${randomString}"
}