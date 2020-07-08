import de.innosystec.kuestion.module
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import org.junit.Test
import kotlin.test.assertEquals

class JVMRouteTests {
    @KtorExperimentalAPI
    @KtorExperimentalLocationsAPI
    @Test
    fun testGet(): Unit = withTestApplication({ module(testing = true) }) {
        handleRequest(HttpMethod.Get, "/").apply {
            assertEquals("Hello Ktor GET Home", response.content)
            assertEquals(HttpStatusCode.OK,response.status())
        }
    }
}