package de.innosystec.kuestion.exposed.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object SurveyTable : IntIdTable() {
    val question = varchar("question", 25)
    val hash = varchar("hash", 6).uniqueIndex()
    val expirationTime = datetime("expirationTime")
    //Anzahl an Antworten? Oder query
}

object AnswerTable: IntIdTable() {
    val survey = (varchar("survey", 6) references SurveyTable.hash)
    val text = varchar("text", 50)
    val counts = integer("counts").default(0)
}

object UserTable: IntIdTable() {
    val username = varchar("username", 15)
    val password = varchar("password", 30)
    val identifier = varchar("identifier", 40)
}

