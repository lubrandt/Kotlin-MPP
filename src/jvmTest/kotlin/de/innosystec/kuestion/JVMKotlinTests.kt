import de.innosystec.kuestion.createDate
import de.innosystec.kuestion.exposed.AnswerTable
import de.innosystec.kuestion.exposed.DatabaseSettings.db
import de.innosystec.kuestion.exposed.SurveyTable
import de.innosystec.kuestion.module
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Before
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
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

    private var mockHash:Int = 42

    @AfterEach
    fun after() {
        //purge db
        transaction(db) {
            SchemaUtils.create(
                SurveyTable,
                AnswerTable
            )
            AnswerTable.deleteAll()
            SurveyTable.deleteAll()
        }
    }

    @BeforeEach
    fun beforeEach() {
        transaction(db) {
            SchemaUtils.create(
                SurveyTable,
                AnswerTable
            )
            mockHash = SurveyTable.insertAndGetId {
                it[question] = "JaNeinDoch"
                it[expirationTime] = createDate("2022-07-10T12:30")
            }.value
            assert(mockHash != 42)

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

    //create a new Survey
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
                """{"question":"Kuchen mit Soße?","answers":[{"survey":42,"text":"japadapadu","counts":0},{"survey":42,"text":"nopenopenope","counts":0}],"expirationTime":"2022-07-10T12:30"}"""
            )
        }.apply {
            //returning hash is random
            assertNotNull(response.content)
        }
    }

    @Test
    fun testUpdateSurveyRoue(): Unit = withTestApplication({ module() }) {
        handleRequest(HttpMethod.Post, "/$mockHash/update") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(
                """{"question":"JaNeinDoch","answers":[{"survey":$mockHash,"text":"jap","counts":5},{"survey":$mockHash,"text":"nope","counts":1},{"survey":$mockHash,"text":"japada","counts":6}],"expirationTime":"2022-12-05T12:30"}"""
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
            setBody("""{"survey":$mockHash,"answer":"nope"}""")
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }


    @Test
    fun testBasicSurveyRoute(): Unit = withTestApplication({ module() }) {

        // correct test
        handleRequest(HttpMethod.Get, "/$mockHash") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(
                """{"question":"JaNeinDoch","answers":[{"survey":$mockHash,"text":"jap","counts":5},{"survey":$mockHash,"text":"nope","counts":1}],"expirationTime":"2022-07-10T12:30"}"""
                , response.content
            )
        }

        // delete test
        handleRequest(HttpMethod.Delete, "/$mockHash") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        // delete a non existing survey test
        handleRequest(HttpMethod.Delete, "/42") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
        }.apply {
            assertEquals(HttpStatusCode.NotFound, response.status())
        }

        // BadRequest after delete
        handleRequest(HttpMethod.Get, "/$mockHash") {
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
    }

    @Test
    fun testEndSurveyRoute(): Unit = withTestApplication({ module() }) {
        handleRequest(HttpMethod.Post, "/endSurvey") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
            addHeader(HttpHeaders.ContentType,ContentType.Application.Json.toString())
            setBody("$mockHash")
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        handleRequest(HttpMethod.Post, "/endSurvey") {
            addHeader(HttpHeaders.Authorization, "Basic cGV0ZXI6cGV0ZXI=") //peter:peter
            addHeader(HttpHeaders.ContentType,ContentType.Application.Json.toString())
            setBody("42")
        }.apply {
            assertEquals(HttpStatusCode.NotFound, response.status())
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