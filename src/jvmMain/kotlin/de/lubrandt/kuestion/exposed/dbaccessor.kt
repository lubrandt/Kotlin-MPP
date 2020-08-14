package de.lubrandt.kuestion.exposed

import de.lubrandt.kuestion.*
import de.lubrandt.kuestion.exposed.DatabaseSettings.db
import de.lubrandt.kuestion.network.CommonDateUtil
import de.lubrandt.kuestion.network.compareToCurrentDate
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime

val dbAccessor = DatabaseInterfaceImpl()

class DatabaseInterfaceImpl : DatabaseInterface {

    override fun createSurvey(survey: SurveyPackage): Int {
        var hash: Int = 0
        loggedSchemaUtilsTransaction(db, SurveyTable, AnswerTable) {
            hash = SurveyTable.insertAndGetId {
                it[question] = survey.question
                it[expirationTime] = createDate(survey.expirationTime)
            }.value

            survey.answers.forEach {
                insertNewAnswer(hash, it.text)
            }
        }
        return hash
    }

    override fun insertNewAnswer(hash: Int, answer: String) {
        loggedSchemaUtilsTransaction(db, AnswerTable) {
            AnswerTable.insert {
                it[survey] = hash
                it[text] = answer
            }
        }
    }

    override fun insertChangedAnswer(hash: Int, answer: String, count: Int) {
        loggedSchemaUtilsTransaction(db, AnswerTable) {
            AnswerTable.insert {
                it[survey] = hash
                it[text] = answer
                it[counts] = count
            }
        }
    }

    override fun addAnswerCount(answer: SurveyAnswerPair) {
        loggedSchemaUtilsTransaction(db, SurveyTable, AnswerTable) loggedSchemaUtilsTransaction@{
            val date =
                SurveyTable.select { SurveyTable.id eq answer.survey }.map { mapToSurvey(it) }.first().expirationTime
            // commonMain Usecase
            if (!CommonDateUtil.compareToCurrentDate(date)) return@loggedSchemaUtilsTransaction
            AnswerTable.update({ (AnswerTable.survey eq answer.survey) and (AnswerTable.text eq answer.answer) }) {
                with(SqlExpressionBuilder) {
                    it[counts] = counts + 1
                }
            }
        }
    }

    override fun endSurvey(hash: Int) {
        loggedSchemaUtilsTransaction(db, SurveyTable) {
            SurveyTable.update({ SurveyTable.id eq hash }) {
                it[expirationTime] = LocalDateTime.now()
            }
        }
    }

    override fun surveyExists(hash: Int): Boolean {
        var exists = 0L
        loggedSchemaUtilsTransaction(db, SurveyTable, AnswerTable) {
            exists = SurveyTable.select { SurveyTable.id eq hash }.count()
        }
        return exists != 0L
    }

    override fun deleteSurvey(hash: Int) {
        loggedSchemaUtilsTransaction(db, SurveyTable, AnswerTable) {
            AnswerTable.deleteWhere { AnswerTable.survey eq hash }
            SurveyTable.deleteWhere { SurveyTable.id eq hash }
        }
    }

    override fun getAnswers(hash: Int): List<Answer> {
        var retval: List<Answer> = mutableListOf()
        loggedSchemaUtilsTransaction(db, SurveyTable, AnswerTable) {
            retval = AnswerTable.select { AnswerTable.survey eq hash }.map { mapToAnswer(it) }
        }
        return retval
    }

    override fun getQuestion(hash: Int): String {
        var question = ""
        loggedSchemaUtilsTransaction(db, SurveyTable) {
            question = SurveyTable.select { SurveyTable.id eq hash }.map { mapToSurvey(it) }.first().question
        }
        return question
    }

    override fun getExpirationTime(hash: Int): String {
        var time = ""
        loggedSchemaUtilsTransaction(db, SurveyTable) {
            time = SurveyTable.select { SurveyTable.id eq hash }
                .map { mapToSurvey(it) }
                .first().expirationTime.toString()
        }
        return time
    }

    override fun includeSurveyChanges(hash: Int, changes: SurveyPackage) {
        loggedSchemaUtilsTransaction(db, SurveyTable, AnswerTable) {
            if (surveyExists(hash)) {
                SurveyTable.update({ SurveyTable.id eq hash }) {
                    it[question] = changes.question
                    it[expirationTime] = createDate(changes.expirationTime)
                }
                AnswerTable.deleteWhere { AnswerTable.survey eq hash }

                changes.answers.forEach {
                    insertChangedAnswer(hash, it.text, it.counts)
                }
            }
        }
    }

    override fun getAllSurveys(): MutableList<HashQuestionPair> {
        val listOfSurveys = mutableListOf<HashQuestionPair>()
        loggedSchemaUtilsTransaction(db, SurveyTable) {
            SurveyTable.selectAll()
                .map { mapToSurvey(it) }
                .forEach { listOfSurveys.add(HashQuestionPair(it.hash, it.question)) }
        }
        return listOfSurveys
    }
}
