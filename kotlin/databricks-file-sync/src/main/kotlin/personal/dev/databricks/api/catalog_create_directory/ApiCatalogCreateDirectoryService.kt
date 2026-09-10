package personal.dev.databricks.api.catalog_create_directory

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import personal.dev.databricks.DatabricksDetailsService
import personal.dev.utils.IApi
import personal.dev.utils.useClient

/**
 * https://docs.databricks.com/api/workspace/files/createdirectory
 */
@Service class ApiCatalogCreateDirectoryService (
    private val databricksDetailsService: DatabricksDetailsService
) : IApi<ApiCatalogCreateDirectoryResponse, ApiCatalogCreateDirectoryRequest> {
    override val title: String = "CREATE_DIRECTORY"
    override val api: String = databricksDetailsService.fsCreateDirectory()

    override fun execute(params: ApiCatalogCreateDirectoryRequest): ApiCatalogCreateDirectoryResponse {
        runBlocking {
            useClient { client ->
                val url = buildUrl(params)
                println(url)
                println(databricksDetailsService.getBearer())
                client.put(url) {
                    header(HttpHeaders.Authorization, databricksDetailsService.getBearer())
                    contentType(ContentType.Application.Json)
                }.bodyAsText().apply { println(this) }
            }
        }
        return ApiCatalogCreateDirectoryResponse()
    }
    override fun buildUrl(params: ApiCatalogCreateDirectoryRequest): String {
        val path = params.path
        return "$api/$path"
    }
}