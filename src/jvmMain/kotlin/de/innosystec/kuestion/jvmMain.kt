package de.innosystec.kuestion

import de.innosystec.kuestion.exposed.HashUtils.ownsha1
import de.innosystec.kuestion.exposed.db.UserTable
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.netty.EngineMain
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.sha1
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

fun main(args: Array<String>) = EngineMain.main(args)


@KtorExperimentalAPI
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
//    transaction {
//        addLogger(StdOutSqlLogger)
//        SchemaUtils.create(UserTable)
//        val tmpUsername = "TestUser"
//        val sha1 = ownsha1(tmpUsername).toString()
//        UserTable.insert {
//            it[username] = tmpUsername
//            it[password] = "1234"
//            it[identifier] = sha1
//        }
//    }

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






