package personal.dev.databricks.api.catalog_delete_directory

import kotlinx.serialization.Serializable
import personal.dev.utils.IRequest
import personal.dev.utils.IResponse

@Serializable data class ApiCatalogDeleteDirectoryRequest (val path: String) : IRequest

@Serializable class ApiCatalogDeleteDirectoryResponse : IResponse