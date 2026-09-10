package personal.dev.databricks.api.catalog_upload_file

import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import personal.dev.databricks.DatabricksDetailsService
import personal.dev.utils.IApi
import personal.dev.utils.useClient

@Service class ApiCatalogUploadFile (
    private val databricksDetailsService: DatabricksDetailsService
) : IApi<ApiCatalogUploadFileResponse, ApiCatalogUploadFileRequest> {
    override val title: String = "UPLOAD_FILE"
    override val api: String = databricksDetailsService.fsUploadFile()

    override fun execute(params: ApiCatalogUploadFileRequest): ApiCatalogUploadFileResponse {
        runBlocking {
            useClient { client ->
                val raw = client.put(buildUrl(params)) {
                    header(HttpHeaders.Authorization, databricksDetailsService.getBearer())
                    contentType(ContentType.Application.Json)
                    setBody(params.bytes)
                    parameter("overwrite", false)
                }.bodyAsText()
                println("UPLOAD FILE:")
                println(raw)
            }
        }
        return ApiCatalogUploadFileResponse()
    }

    override fun buildUrl(params: ApiCatalogUploadFileRequest): String {
        val filepath = params.filepath
        return "$api/$filepath"
    }
}