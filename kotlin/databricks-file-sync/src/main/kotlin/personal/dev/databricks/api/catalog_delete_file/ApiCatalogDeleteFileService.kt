package personal.dev.databricks.api.catalog_delete_file

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import personal.dev.databricks.DatabricksDetailsService
import personal.dev.utils.IApi
import personal.dev.utils.useClient

/**
 * https://docs.databricks.com/api/workspace/files/delete
 */
@Service class ApiCatalogDeleteFileService (
    private val databricksDetailsService: DatabricksDetailsService
) : IApi<ApiCatalogDeleteFileResponse, ApiCatalogDeleteFileRequest> {
    override val title: String = "DELETE_FILE"
    override val api: String = databricksDetailsService.fsDeleteFile()

    override fun execute(params: ApiCatalogDeleteFileRequest): ApiCatalogDeleteFileResponse {
        runBlocking {
            useClient { client ->
                client.delete(buildUrl(params)) {
                    header(HttpHeaders.Authorization, databricksDetailsService.getBearer())
                    contentType(ContentType.Application.Json)
                }.bodyAsText().apply { println(this) }
            }
        }
        return ApiCatalogDeleteFileResponse()
    }
    override fun buildUrl(params: ApiCatalogDeleteFileRequest): String {
        val path = params.filepath
        return "$api/$path"
    }
}