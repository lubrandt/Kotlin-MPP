package de.innosystec.kuestion.exposed

import de.innosystec.kuestion.*
import de.innosystec.kuestion.exposed.db.AnswerTable
import de.innosystec.kuestion.exposed.db.DatabaseSettings.db
import de.innosystec.kuestion.exposed.db.SurveyTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import kotlin.random.Random

data class Survey(val question: String, val hash: String, val expirationTime: LocalDateTime)

fun createSurvey(survey: SurveyPackage): String {
    var hash = ""
    transaction(db) {
        SchemaUtils.create(SurveyTable, AnswerTable)
        hash = createSurvey(survey.question, createDate(survey.expirationTime))
        survey.answers.forEach {
            insertNewAnswer(hash, it.text)
        }
    }
    return hash
}

fun createSurvey(question: String, expirationTime: LocalDateTime): String {
    val tmpHash = createHash()
    transaction(db) {
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

fun insertNewAnswer(tmpSurvey: String, tmpAnswer: String) {
    transaction(db) {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(AnswerTable)
        AnswerTable.insert {
            it[survey] = tmpSurvey
            it[text] = tmpAnswer
        }
    }
}

fun insertChangedAnswer(tmpSurvey: String, tmpAnswer: String, tmpCount: Int) {
    transaction(db) {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(AnswerTable)
        AnswerTable.insert {
            it[survey] = tmpSurvey
            it[text] = tmpAnswer
            it[counts] = tmpCount
        }
    }
}

fun addAnswerCount(answer: StringPair) {
    transaction(db) {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(SurveyTable, AnswerTable)
        val date =
            SurveyTable.select { SurveyTable.hash eq answer.first }.map { mapToSurvey(it) }.first().expirationTime
        // commonMain Usecase
        if (!CommonDateUtil.checkDate(date)) return@transaction
        AnswerTable.update({ (AnswerTable.survey eq answer.first) and (AnswerTable.text eq answer.second) }) {
            with(SqlExpressionBuilder) {
                it[counts] = counts + 1
            }
        }
    }
}

fun endSurvey(id: String) {
    transaction(db) {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(SurveyTable)
        SurveyTable.update({ SurveyTable.hash eq id }) {
            it[expirationTime] = LocalDateTime.now()
        }
    }
}

fun surveyExists(hash: String): Boolean {
    var exists = 0L
    transaction(db) {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(SurveyTable, AnswerTable)
        exists = SurveyTable.select { SurveyTable.hash eq hash }.count()
    }
    return exists != 0L
}

fun deleteSurvey(hash: String) {
    transaction(db) {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(SurveyTable, AnswerTable)
        AnswerTable.deleteWhere { AnswerTable.survey eq hash }
        SurveyTable.deleteWhere { SurveyTable.hash eq hash }
    }
}

fun getAnswers(hash: String): List<Answer> {
    var retval: List<Answer> = mutableListOf()
    transaction(db) {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(AnswerTable, SurveyTable)
        retval = AnswerTable.select { AnswerTable.survey eq hash }.map { mapToAnswer(it) }
    }
    return retval
}

fun getQuestion(hash: String): String {
    var question = ""
    transaction(db) {
        question = SurveyTable.select { SurveyTable.hash eq hash }.map { mapToSurvey(it) }.first().question
    }
    return question
}

fun getExpirationTime(hash: String): String {
    var time = ""
    transaction(db) {
        time = SurveyTable.select { SurveyTable.hash eq hash }
            .map { mapToSurvey(it) }
            .first().expirationTime.toString()
    }
    return time
}

fun includeSurveyChanges(hash: String, changes: SurveyPackage): Boolean {
    if (surveyExists(hash)) {
        transaction(db) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(AnswerTable, SurveyTable)
            SurveyTable.update({ SurveyTable.hash eq hash }) {
                it[question] = changes.question
                it[expirationTime] = createDate(changes.expirationTime)
            }
            AnswerTable.deleteWhere { AnswerTable.survey eq hash }
        }

        changes.answers.forEach {
            insertChangedAnswer(hash, it.text, it.counts)
        }
        return true
    } else return false

}

fun getAllCreatedSurveys(): MutableList<StringPair> {
    val listOfSurveys = mutableListOf<StringPair>()
    transaction(db) {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(SurveyTable)
        SurveyTable.selectAll()
            .map { mapToSurvey(it) }
            .forEach { listOfSurveys.add(StringPair(it.question, it.hash)) }
    }
    return listOfSurveys
}



fun mapToSurvey(it: ResultRow) = Survey(
    question = it[SurveyTable.question],
    hash = it[SurveyTable.hash],
    expirationTime = it[SurveyTable.expirationTime]
)

fun mapToAnswer(it: ResultRow) = Answer(
    survey = it[AnswerTable.survey],
    text = it[AnswerTable.text],
    counts = it[AnswerTable.counts]
)