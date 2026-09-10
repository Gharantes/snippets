package personal.dev.databricks.api.catalog_list

import kotlinx.serialization.Serializable
import personal.dev.utils.IRequest
import personal.dev.utils.IResponse

@Serializable data class ApiCatalogListContentsRequest (
    val path: String
) : IRequest

@Serializable data class ApiCatalogListContentsResponse (
    val contents: List<ApiCatalogListContentsResponseNode>? = null,
    val next_page_token: String? = null
) : IResponse

@Serializable data class ApiCatalogListContentsResponseNode (
    val file_size: Long? = null,
    val is_directory: Boolean,
    val last_modified: Long? = null,
    val name: String,
    val path: String
)