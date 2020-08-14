package de.lubrandt.kuestion.exposed

import de.lubrandt.kuestion.Answer
import org.jetbrains.exposed.sql.ResultRow

fun mapToSurvey(it: ResultRow) = Survey(
    question = it[SurveyTable.question],
    hash = it[SurveyTable.id].value,
    expirationTime = it[SurveyTable.expirationTime]
)

fun mapToAnswer(it: ResultRow) = Answer(
    survey = it[AnswerTable.survey],
    text = it[AnswerTable.text],
    counts = it[AnswerTable.counts]
)

