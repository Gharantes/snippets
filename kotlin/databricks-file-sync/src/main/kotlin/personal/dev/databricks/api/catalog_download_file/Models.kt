package personal.dev.databricks.api.catalog_download_file

import kotlinx.serialization.Serializable
import personal.dev.utils.IRequest
import personal.dev.utils.IResponse

@Serializable data class ApiCatalogDownloadFileRequest (val filepath: String) : IRequest
@Serializable data class ApiCatalogDownloadFileResponse (val bytes: ByteArray?) : IResponse
