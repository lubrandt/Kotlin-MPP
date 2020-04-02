package de.innosystec.kuestion.exposed.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime
//import org.jetbrains.exposed.sql.jodatime.datetime

object SurveyTable : IntIdTable() {
    val question = varchar("question", 25)
    val hash = integer("hashcode").uniqueIndex()
    val expirationTime = datetime("expirationTime")
//    val test = datetime("woob")
}

object AnswerTable: IntIdTable() {
    val survey = (integer("surveyHash") references SurveyTable.hash)
    val text = varchar("text", 50).uniqueIndex()
    val counts = integer("counts").default(0)
}

