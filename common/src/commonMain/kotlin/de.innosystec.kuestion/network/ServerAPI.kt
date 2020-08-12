package de.innosystec.kuestion.network

import de.innosystec.kuestion.HashQuestionPair
import de.innosystec.kuestion.SurveyAnswerPair
import de.innosystec.kuestion.SurveyPackage

interface ServerAPI {
    /**
     * get the Survey for the given id
     * @param id survey
     */
    suspend fun getSurveyFromApi(id: Int): SurveyPackage

    /**
     * sends the survey
     * @param survey
     * @return the survey id
     */
    suspend fun sendSurveyToApi(survey: SurveyPackage): Int

    /**
     * sends the clicked answer
     * @param answer
     */
    suspend fun sendClickedAnswerToApi(answer: SurveyAnswerPair)

    /**
     * get all Surveys
     * @return List of Surveys
     */
    suspend fun getAllSurveys(): List<HashQuestionPair>

    /**
     * delete the survey with the pecified id
     * @param id
     */
    suspend fun deleteSurvey(id: Int)

    /**
     * end voting on the survey with the pecified id
     * @param id
     */
    suspend fun endSurvey(id:Int)

    /**
     * send the changed survey
     * @param id
     * @param survey
     */
    suspend fun sendChangedSurvey(id: Int, survey: SurveyPackage)
}