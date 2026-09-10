package personal.dev.databricks.api.catalog_delete_file

import kotlinx.serialization.Serializable
import personal.dev.utils.IRequest
import personal.dev.utils.IResponse

@Serializable data class ApiCatalogDeleteFileRequest (val filepath: String) : IRequest
@Serializable class ApiCatalogDeleteFileResponse : IResponse