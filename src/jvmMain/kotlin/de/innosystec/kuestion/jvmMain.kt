package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.*
import de.innosystec.kuestion.exposed.db.AnswerTable
import de.innosystec.kuestion.exposed.db.SurveyTable
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.receive
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
import java.time.LocalDateTime

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
        allowCredentials = true
        allowNonSimpleContentTypes = true
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
        getSurvey()
        postSurvey()
    }
}






