package personal.dev.databricks.api.catalog_download_file

import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import personal.dev.databricks.DatabricksDetailsService
import personal.dev.utils.IApi
import personal.dev.utils.useClient

/**
 * https://docs.databricks.com/api/workspace/files/download
 */
@Service class ApiCatalogDownloadFileService (
    private val databricksDetailsService: DatabricksDetailsService
) : IApi<ApiCatalogDownloadFileResponse, ApiCatalogDownloadFileRequest> {
    override val title: String = "DOWNLOAD_FILE"
    override val api: String = databricksDetailsService.fsDownloadFile()

    override fun execute(params: ApiCatalogDownloadFileRequest): ApiCatalogDownloadFileResponse {
        var response = ApiCatalogDownloadFileResponse(null)
        runBlocking {
            useClient { client ->
                val bytes: ByteArray = client.get(buildUrl(params)) {
                    header(HttpHeaders.Authorization, databricksDetailsService.getBearer())
                }.body()
                response = ApiCatalogDownloadFileResponse(bytes)
            }
        }
        return response
    }

    override fun buildUrl(params: ApiCatalogDownloadFileRequest): String {
        val filepath = params.filepath
        return "$api/$filepath"
    }
}