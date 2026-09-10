package personal.dev.databricks.api.catalog_list

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import personal.dev.databricks.DatabricksDetailsService
import personal.dev.utils.IApi
import personal.dev.utils.useClient

/**
 * https://docs.databricks.com/api/workspace/files/listdirectorycontents
 */
@Service class ApiCatalogListDirectoryContentsService (
    private val databricksDetailsService: DatabricksDetailsService
) : IApi<ApiCatalogListContentsResponse, ApiCatalogListContentsRequest> {
    override val title: String = "LIST_DIRECTORY_CONTENTS"
    override val api: String = databricksDetailsService.fsListDirectoryContent()

    override fun execute(params: ApiCatalogListContentsRequest): ApiCatalogListContentsResponse {
        var res = ApiCatalogListContentsResponse(emptyList(), "string")
        runBlocking {
            useClient { client ->
                val raw = client.get(buildUrl(params)) {
                    header(HttpHeaders.Authorization, databricksDetailsService.getBearer())
                    contentType(ContentType.Application.Json)
                }.bodyAsText().apply { println(this) }
                res = Json.decodeFromString<ApiCatalogListContentsResponse>(raw)
            }

        }
        return res
    }

    override fun buildUrl(params: ApiCatalogListContentsRequest): String {
        val path = params.path
        return "$api/$path"
    }
}