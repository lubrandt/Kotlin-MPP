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

fun createSurvey(question: String, expirationTime: LocalDateTime): String {
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

fun insertNewAnswer(tmpSurvey: String, tmpAnswer: String) {
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

fun insertChangedAnswer(tmpSurvey: String, tmpAnswer: String, tmpCount: Int, tmpColor: String) {
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(AnswerTable)
        AnswerTable.insert {
            it[survey] = tmpSurvey
            it[text] = tmpAnswer
            it[counts] = tmpCount
            it[color] = tmpColor
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

fun deleteSurvey(hash: String) {
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(SurveyTable, AnswerTable)
        AnswerTable.deleteWhere { AnswerTable.survey eq hash }
        SurveyTable.deleteWhere { SurveyTable.hash eq hash }
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

fun includeSurveyChanges(hash: String, changes: SurveyCreation) {
    val oldAnswers = mutableListOf<ChartSliceData>()
    val changedAnswers = mutableListOf<ChartSliceData>()
    val nextAnswers = mutableListOf<ChartSliceData>()

    println("FIRST")
    println("oldAnswers: $oldAnswers")
    println("changedAnswers: $changedAnswers")
    println("nextAnswers: $nextAnswers")

    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(AnswerTable, SurveyTable)
        SurveyTable.update({ SurveyTable.hash eq hash }) {
            it[question] = changes.question
            it[expirationTime] = createDate(changes.expirationTime)
        }

        getAnswers(hash).forEach { oldAnswers.add(ChartSliceData(it.text, it.counts, it.color)) }

        AnswerTable.deleteWhere { AnswerTable.survey eq hash }
    }
    println("SECOND")
    println("oldAnswers: $oldAnswers")
    println("changedAnswers: $changedAnswers")
    println("nextAnswers: $nextAnswers")

    changes.answers.forEach {
        changedAnswers.add(ChartSliceData(it,0, randHexColor()))
    }
    println("THIRD")
    println("oldAnswers: $oldAnswers")
    println("changedAnswers: $changedAnswers")
    println("nextAnswers: $nextAnswers")

    // what if typos changed? currently it is a completly new answer and all counts are lost
    oldAnswers.forEach { old ->
        changedAnswers.forEach { changed ->
            if (old.title == changed.title) {
                nextAnswers.add(old)
            }
        }
    }
    println("FOURTH")
    println("oldAnswers: $oldAnswers")
    println("changedAnswers: $changedAnswers")
    println("nextAnswers: $nextAnswers")
    changedAnswers.forEach { changed ->
        nextAnswers.forEach { next ->
            if (changed == next) {
                nextAnswers.remove(next)
                nextAnswers.add(changed)
            }
        }
    }
    println("FIFTH")
    println("oldAnswers: $oldAnswers")
    println("changedAnswers: $changedAnswers")
    println("nextAnswers: $nextAnswers")

    nextAnswers.forEach {
        insertChangedAnswer(hash, it.title, it.value, it.color)
    }
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