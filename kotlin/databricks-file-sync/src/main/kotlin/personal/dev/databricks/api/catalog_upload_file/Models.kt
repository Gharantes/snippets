package personal.dev.databricks.api.catalog_upload_file

import kotlinx.serialization.Serializable
import personal.dev.utils.IRequest
import personal.dev.utils.IResponse

@Serializable data class ApiCatalogUploadFileRequest (
    val filepath: String,
    val bytes: ByteArray
) : IRequest

@Serializable class ApiCatalogUploadFileResponse : IResponse
