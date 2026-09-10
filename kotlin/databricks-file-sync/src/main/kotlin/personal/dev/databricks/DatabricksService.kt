package personal.dev.databricks

import org.springframework.stereotype.Service
import personal.dev.databricks.api.catalog_create_directory.ApiCatalogCreateDirectoryService
import personal.dev.databricks.api.catalog_create_directory.ApiCatalogCreateDirectoryRequest
import personal.dev.databricks.api.catalog_delete_directory.ApiCatalogDeleteDirectoryRequest
import personal.dev.databricks.api.catalog_delete_directory.ApiCatalogDeleteDirectoryService
import personal.dev.databricks.api.catalog_delete_file.ApiCatalogDeleteFileRequest
import personal.dev.databricks.api.catalog_delete_file.ApiCatalogDeleteFileService
import personal.dev.databricks.api.catalog_download_file.ApiCatalogDownloadFileRequest
import personal.dev.databricks.api.catalog_download_file.ApiCatalogDownloadFileResponse
import personal.dev.databricks.api.catalog_download_file.ApiCatalogDownloadFileService
import personal.dev.databricks.api.catalog_list.ApiCatalogListContentsRequest
import personal.dev.databricks.api.catalog_list.ApiCatalogListContentsResponse
import personal.dev.databricks.api.catalog_list.ApiCatalogListDirectoryContentsService
import personal.dev.databricks.api.catalog_upload_file.ApiCatalogUploadFile
import personal.dev.databricks.api.catalog_upload_file.ApiCatalogUploadFileRequest
import java.io.File

@Service
class DatabricksService (
    private val listContentsService: ApiCatalogListDirectoryContentsService,
    private val createDirectoryService: ApiCatalogCreateDirectoryService,
    private val deleteDirectoryService: ApiCatalogDeleteDirectoryService,
    private val deleteFileService: ApiCatalogDeleteFileService,
    private val downloadFileService: ApiCatalogDownloadFileService,
    private val uploadFileService: ApiCatalogUploadFile,
) {
    /* ===================================================================
     *                            Directories
     * ================================================================ */
    fun createDirectory(path: String) {
        val request = ApiCatalogCreateDirectoryRequest(path)
        createDirectoryService.execute(request)
    }
    fun deleteDirectory(path: String) {
        val request = ApiCatalogDeleteDirectoryRequest(path)
        deleteDirectoryService.execute(request)
    }
    fun getDirectoryContent(directory: String): ApiCatalogListContentsResponse {
        val request = ApiCatalogListContentsRequest(directory)
        val response = listContentsService.execute(request)
        println(response)
        return response
    }
    /* ===================================================================
     *                             File(s)
     * ================================================================ */
    fun uploadFile(filepath: String, file: File) {
        val request = ApiCatalogUploadFileRequest(filepath, file.readBytes())
        uploadFileService.execute(request)
    }
    fun deleteFile(filepath: String) {
        val request = ApiCatalogDeleteFileRequest(filepath)
        deleteFileService.execute(request)
    }
    fun downloadFile(filepath: String): ApiCatalogDownloadFileResponse {
        val request = ApiCatalogDownloadFileRequest(filepath)
        return downloadFileService.execute(request)
    }
}