package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.db.AnswerTable
import de.innosystec.kuestion.exposed.db.DatabaseSettings.db
import de.innosystec.kuestion.exposed.db.SurveyTable
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * body structure:
 * {"question":"","answers":[{"survey":"abcdef","text":"jap","counts":5},{"survey":"abcdef","text":"nope","counts":1}],"expirationTime":"2020-07-10T12:34:03.904216"}
 */

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
class JVMRouteTests {


    @AfterEach
    fun after() {
        //purge db
        transaction(db) {
            SchemaUtils.create(SurveyTable, AnswerTable)
            AnswerTable.deleteAll()
            SurveyTable.deleteAll()
        }
    }

    @BeforeEach

    fun before() {

        val mockHash = "abcdef"
        transaction(db) {
            SchemaUtils.create(SurveyTable, AnswerTable)
            SurveyTable.insert {
                it[question] = "JaNeinDoch"
                it[hash] = mockHash
                it[expirationTime] = createDate("2022-07-10T12:30")
            }
            AnswerTable.insert {
                it[survey] = mockHash
                it[text] = "jap"
                it[counts] = 5
            }
            AnswerTable.insert {
                it[survey] = mockHash
                it[text] = "nope"
                it[counts] = 1
            }
        }
    }

    @Test
    fun testHomeRoute(): Unit = withTestApplication({ module() }) {
        handleRequest(HttpMethod.Get, "/") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
        }.apply {
            assertEquals("Hello Ktor GET Home", response.content)
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun testPostSurveyRoute(): Unit = withTestApplication({ module() }) {

//        Assertions.assertAll(Executable {
//            assertTrue { true }
//        }, Executable {
//
//        }, Executable { })

        handleRequest(HttpMethod.Post, "/postSurvey") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(
                """{"question":"Kuchen?","answers":[{"survey":"","text":"jap","counts":5},{"survey":"","text":"nope","counts":1}],"expirationTime":"2022-07-10T12:30"}"""
            )
        }.apply {
            //todo: returning hash is random
            assertNotNull(response.content)
        }
    }

    @Test
    fun testUpdateSurveyRoue(): Unit = withTestApplication({ module() }) {
        handleRequest(HttpMethod.Post, "/abcdef/update") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(
                """{"question":"JaNeinDoch","answers":[{"survey":"abcdef","text":"jap","counts":5},{"survey":"abcdef","text":"nope","counts":1},{"survey":"abcdef","text":"japada","counts":6}],"expirationTime":"2022-12-05T12:30"}"""
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun testIncreaseCounterRoute(): Unit = withTestApplication({ module() }) {
        handleRequest(HttpMethod.Post, "/inc") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"first":"abcdef","second":"nope"}""")
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
//            assertEquals("""{"first":"abcdef","second":"nope"}""", response.content)
        }
    }


    @Test
    fun testBasicSurveyRoute(): Unit = withTestApplication({ module() }) {
        //todo: each into their own function?

        // correct test
        handleRequest(HttpMethod.Get, "/abcdef") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(
                """{"question":"JaNeinDoch","answers":[{"survey":"abcdef","text":"jap","counts":5},{"survey":"abcdef","text":"nope","counts":1}],"expirationTime":"2022-07-10T12:30"}"""
                , response.content
            )
        }

        // delete test
        handleRequest(HttpMethod.Delete, "/abcdef") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        // delete a non existing survey test
        handleRequest(HttpMethod.Delete, "/gggggg") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }

        // BadRequest after delete
        handleRequest(HttpMethod.Get, "/abcdef") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }

        // survey does not exist
        handleRequest(HttpMethod.Get, "/gggggg") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }

        //hash length != 6
        handleRequest(HttpMethod.Get, "/abcdef7") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
    }

    @Test
    fun testEndSurveyRoute(): Unit = withTestApplication({ module() }) {
        handleRequest(HttpMethod.Post, "/endSurvey") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
            setBody("\"abcdef\"")
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        handleRequest(HttpMethod.Post, "/endSurvey") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
            setBody("\"abcdef7\"")
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
    }

    @Test
    fun testAllSurveysRoute(): Unit = withTestApplication({ module() }) {
        handleRequest(HttpMethod.Get, "/allSurveys") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
//            assertEquals("", response.content)
        }
    }
}