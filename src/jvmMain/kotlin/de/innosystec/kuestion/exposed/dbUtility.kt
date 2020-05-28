package de.innosystec.kuestion.exposed

import de.innosystec.kuestion.*
import de.innosystec.kuestion.exposed.db.AnswerTable
import de.innosystec.kuestion.exposed.db.SurveyTable
import de.innosystec.kuestion.exposed.db.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.StringBuilder
import java.time.LocalDateTime
import kotlin.random.Random
import java.security.MessageDigest
import java.security.SecureRandom

data class Survey(val question: String, val hash: String, val expirationTime: LocalDateTime)

data class Answer(val surveyHashCode: String, val text: String, val counts: Int = 0, val color: String)

data class User(val username: String, val password: String, val identifier: String)

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
        SchemaUtils.create(SurveyTable, AnswerTable)
        val date =
            SurveyTable.select { SurveyTable.hash eq answer.surveyHash }.map { mapToSurvey(it) }.first().expirationTime
        if (LocalDateTime.now() > date) return@transaction
        AnswerTable.update({ (AnswerTable.survey eq answer.surveyHash) and (AnswerTable.text eq answer.answer) }) {
            with(SqlExpressionBuilder) {
                it[counts] = counts + 1
            }
        }
    }
}

fun endSurvey(id: String) {
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(SurveyTable)
        SurveyTable.update({ SurveyTable.hash eq id }) {
            it[expirationTime] = LocalDateTime.now()
        }
    }
}

fun getAnswers(hash: String): List<Answer> {
    var retval: List<Answer> = mutableListOf()
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(AnswerTable, SurveyTable)
        retval = AnswerTable.select { AnswerTable.survey eq hash }.map { mapToAnswer(it) }
    }
    return retval
}

fun mapToSurvey(it: ResultRow) = Survey(
    question = it[SurveyTable.question],
    hash = it[SurveyTable.hash],
    expirationTime = it[SurveyTable.expirationTime]
)

fun mapToAnswer(it: ResultRow) = Answer(
    surveyHashCode = it[AnswerTable.survey],
    text = it[AnswerTable.text],
    counts = it[AnswerTable.counts],
    color = it[AnswerTable.color]
)

fun mapUser(it: ResultRow) = User(
    it[UserTable.username],
    it[UserTable.password],
    it[UserTable.identifier]
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

/**
 * https://www.samclarke.com/kotlin-hash-strings/
 * 19.05.2020 15:15
 * without the secureRandom things
 */
object HashUtils {
    fun ownsha1(username: String) = hashString("SHA1", username)

    private fun hashString(algorithm: String, input: String): String {
        val HEX_CHARS = "0123456789ABCDEF"
        val secureRandom = SecureRandom()
        val salt = secureRandom.nextInt(100) //is this really a salt?
        val bytes = MessageDigest.getInstance(algorithm).digest((input + salt).toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }
}