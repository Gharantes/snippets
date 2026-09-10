package personal.dev.utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

suspend fun useClient(fn: suspend (HttpClient) -> Unit) {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) { json(Json {
            ignoreUnknownKeys = true
        }) }
    }
    try {
        fn(client)
    } catch (e: Exception) {
        throw e
    } finally {
        client.close()
    }
}



