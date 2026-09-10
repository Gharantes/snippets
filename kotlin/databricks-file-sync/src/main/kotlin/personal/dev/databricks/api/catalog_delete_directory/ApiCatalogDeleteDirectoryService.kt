package personal.dev.databricks.api.catalog_delete_directory

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import personal.dev.databricks.DatabricksDetailsService
import personal.dev.utils.IApi
import personal.dev.utils.useClient

/**
 * https://docs.databricks.com/api/workspace/files/deletedirectory
 */
@Service class ApiCatalogDeleteDirectoryService (
    private val databricksDetailsService: DatabricksDetailsService
) : IApi<ApiCatalogDeleteDirectoryResponse, ApiCatalogDeleteDirectoryRequest> {
    override val title: String = "DELETE_DIRECTORY"
    override val api: String = databricksDetailsService.fsDeleteDirectory()

    override fun execute(params: ApiCatalogDeleteDirectoryRequest): ApiCatalogDeleteDirectoryResponse {
        runBlocking {
            useClient { client ->
                client.delete(buildUrl(params)) {
                    header(HttpHeaders.Authorization, databricksDetailsService.getBearer())
                    contentType(ContentType.Application.Json)
                }.bodyAsText().apply { println(this) }
            }
        }
        return ApiCatalogDeleteDirectoryResponse()
    }
    override fun buildUrl(params: ApiCatalogDeleteDirectoryRequest): String {
        val path = params.path
        return "$api/$path"
    }
}