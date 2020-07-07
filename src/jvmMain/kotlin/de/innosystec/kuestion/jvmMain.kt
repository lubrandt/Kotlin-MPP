package de.innosystec.kuestion

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.netty.EngineMain
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.*

fun main(args: Array<String>) = EngineMain.main(args)


@KtorExperimentalLocationsAPI
@KtorExperimentalAPI
internal fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    install(DefaultHeaders)
    install(CallLogging)
    install(Locations)

    install(CORS) {
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Delete)
        method(HttpMethod.Options)
        header(HttpHeaders.AccessControlAllowOrigin)
        header(HttpHeaders.AccessControlAllowHeaders)
        header(HttpHeaders.AccessControlAllowCredentials)
        header(HttpHeaders.WWWAuthenticate)
        header(HttpHeaders.Authorization)
        anyHost()
        allowCredentials = true
        allowNonSimpleContentTypes = true
        allowSameOrigin = true
    }

    install(Compression) {
        gzip()
    }

    install(StatusPages) {
        statusFile(HttpStatusCode.NotFound, filePattern = "error#.html")
    }

    install(Authentication) {
        basic("basic") {
            realm = "Ktor Server"
            validate { credentials ->
                if (credentials.password == credentials.name) UserIdPrincipal(credentials.name) else null
            }
        }
    }

//    Database.connect("jdbc:h2:file:C:/data/sample;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")

    routing {
        home()
        endSurvey()
        counter()
        updateSurvey()
        allSurveys()
        postSurvey()
        getSurvey()
    }
}






