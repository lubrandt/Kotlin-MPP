package de.lubrandt.kuestion.exposed

import de.lubrandt.kuestion.*

interface DatabaseInterface {
    /**
     * inserts a Survey into the db and returns the unique id for it.
     * @param survey complete Survey
     * @return survey id
     */
    fun createSurvey(survey: SurveyPackage): Int

    /**
     * inserts the given answer into the survey with the provided id
     * @param hash survey id
     * @param answer
     */
    fun insertNewAnswer(hash: Int, answer: String)

    /**
     * inserts the given changed answer, hence the count, into the survey with the provided id
     * @param hash
     * @param answer
     * @param count
     */
    fun insertChangedAnswer(hash: Int, answer: String, count: Int)

    /**
     * includes the changes for the given survey id into the db
     * @param hash survey id
     * @param changes changed survey
     */
    fun includeSurveyChanges(hash: Int, changes: SurveyPackage)

    /**
     * increses the count for the given answer by 1
     * @param answer
     */
    fun addAnswerCount(answer: SurveyAnswerPair)

    /**
     * ends the survey with the given id
     * @param hash
     */
    fun endSurvey(hash: Int)

    /**
     * checks if a survey with the given id exists
     * @param hash
     * @return true if it exists else false
     */
    fun surveyExists(hash: Int): Boolean

    /**
     * deletes the survey with the given id
     * @param hash
     */
    fun deleteSurvey(hash: Int)

    /**
     * get all Answers for given survey id
     * @param hash
     * @return List of Answers for given survey id
     */
    fun getAnswers(hash: Int): List<Answer>

    /**
     * get the question of the given survey
     * @param hash
     * @return Question
     */
    fun getQuestion(hash: Int): String

    /**
     * get the expiration Time of the given survey
     * @param hash
     * @return Expiration Time
     */
    fun getExpirationTime(hash: Int): String

    /**
     * get all surveys
     * @return the list of Surveys
     */
    fun getAllSurveys(): MutableList<HashQuestionPair>
}