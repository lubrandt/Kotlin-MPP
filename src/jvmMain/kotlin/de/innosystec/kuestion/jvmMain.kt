package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.Answer
import de.innosystec.kuestion.exposed.Survey
import de.innosystec.kuestion.exposed.db.AnswerTable
import de.innosystec.kuestion.exposed.db.SurveyTable
import de.innosystec.kuestion.exposed.getAnswers
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.DefaultJsonConfiguration
import io.ktor.serialization.json
import io.ktor.serialization.serialization
import io.ktor.server.netty.EngineMain
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>) = EngineMain.main(args)


internal fun Application.module() {
//    val config = environment.config
    install(ContentNegotiation) {
        json(
//            contentType = ContentType.Application.Json,
//            json = Json(
//                DefaultJsonConfiguration.copy(
//                    prettyPrint = true
//                )
//            )
        )
    }

    install(CORS) {
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Delete)
        anyHost()
//        allowCredentials = true
    }

    install(Compression) {
        gzip()
    }

    install(StatusPages) {
        statusFile(HttpStatusCode.NotFound, filePattern = "error#.html")
    }

    install(Routing)

//    Database.connect("jdbc:h2:file:C:/data/sample;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")

    routing {
        home()
        route("/survey_not_found") {
            get {
                call.respond("Sorry, this survey does not exist")
            }
        }
        fetchSurvey()
    }
}

internal fun Routing.home() {
    route("/") {
        get {
            call.respondText("Hello Ktor Home")
        }
        post {
            // create survey here
        }
    }
    // is this needed?
    static("/static") {
        resource("prod-dash.js")
    }
}

internal fun Routing.fetchSurvey() {
    route("/{questionId}") {
        get {
            val hash = call.parameters["questionId"]
            val data = mutableListOf<ChartSliceData>()
            var exists = 0L
            if (hash != null) {
                if (hash.length != 6) {
                    dataMock.forEach { data.add(it) }
                    data.add(ChartSliceData("hashIdTooLongOrTooShort", 5, "#77bcbd"))
                    call.respond(data)
                }
            }
            transaction {
                addLogger(StdOutSqlLogger)
                SchemaUtils.create(SurveyTable, AnswerTable)
                exists = SurveyTable.select { SurveyTable.hash eq hash.toString() }.count()
            }
            if (exists == 0L) {
//                call.respondRedirect("/survey_not_found") // Error Handling in Frontend?
                // what if no results?
                dataMock.forEach { data.add(it) }
                data.add(ChartSliceData("notfound", 15, "#006400"))
                call.respond(data)
            } else {
                var answerList: List<Answer> = mutableListOf()
                transaction {
                    addLogger(StdOutSqlLogger)
                    SchemaUtils.create(SurveyTable, AnswerTable)
                    if (hash != null) {
                        answerList = getAnswers(hash)
                    }
                }
                answerList.forEach {
                    data + ChartSliceData(it.text, it.counts, randHexColor())
                }
                dataMock.forEach { data + it }
                data.add(ChartSliceData("found", 10, "#6224B8"))
                call.respond(data)
            }
        }
    }
}


internal fun randHexColor(): String {
    val stringLength = 6
    val charPool: List<Char> = ('a'..'f') + ('0'..'9')
    val randomString =
        (1..stringLength)
            .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map { x -> charPool[x] }
            .joinToString("")
    return "#${randomString}"
}
