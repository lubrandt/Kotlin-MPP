package de.innosystec.kuestion

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.netty.EngineMain
import org.jetbrains.exposed.sql.Database
import java.util.*

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

    install(Authentication) {
        basic("basicAuth") {
            validate { credentials ->
                if (credentials.password == credentials.name) UserIdPrincipal(credentials.name) else null
            }
        }
        form(name = "myauth2") {
            userParamName = "user"
            passwordParamName = "password"
            challenge {}

            validate { credentials -> if (credentials.password == credentials.name) UserIdPrincipal(credentials.name) else null }
        }
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
        endSurvey()
        counter()
        updateSurvey()
        allSurveys()
        postSurvey()
        getSurvey()
    }
}






