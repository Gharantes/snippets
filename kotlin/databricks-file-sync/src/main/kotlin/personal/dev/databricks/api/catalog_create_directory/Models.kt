package personal.dev.databricks.api.catalog_create_directory

import kotlinx.serialization.Serializable
import personal.dev.utils.IRequest
import personal.dev.utils.IResponse

@Serializable data class ApiCatalogCreateDirectoryRequest (val path: String) : IRequest

@Serializable class ApiCatalogCreateDirectoryResponse : IResponse