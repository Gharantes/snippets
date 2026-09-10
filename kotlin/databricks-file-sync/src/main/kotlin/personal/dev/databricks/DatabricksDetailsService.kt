package personal.dev.databricks

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class DatabricksDetailsService {
    @Value("\${instance-url}") private lateinit var instanceUrl: String
    @Value("\${token}") private lateinit var token: String

    fun getBearer() = "Bearer $token"
    private fun getBaseUrl() = "$instanceUrl/api/2.0"

    fun fsListDirectoryContent() = "${getBaseUrl()}/fs/directories"
    fun fsCreateDirectory() = "${getBaseUrl()}/fs/directories"
    fun fsDeleteDirectory() = "${getBaseUrl()}/fs/directories"
    fun fsDownloadFile() = "${getBaseUrl()}/fs/files"
    fun fsDeleteFile() = "${getBaseUrl()}/fs/files"
    fun fsUploadFile() = "${getBaseUrl()}/fs/files"
}
