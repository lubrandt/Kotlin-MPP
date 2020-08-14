package de.lubrandt.kuestion.exposed

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime

// id is unique identifier
// names need to be set otherwise breakes
object SurveyTable : IntIdTable() {
    val question = varchar("question", 25)
    val expirationTime = datetime("expirationTime")
}

object AnswerTable : IntIdTable() {
    val survey = integer("survey").references(SurveyTable.id)
    val text = varchar("text", 50)
    val counts = integer("counts").default(0)
}


