package personal.dev.file_sync

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import personal.dev.databricks.DatabricksService
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import java.sql.Types
import org.springframework.dao.EmptyResultDataAccessException

@Service
class FileSyncService (
    private val template: NamedParameterJdbcTemplate,
    private val databricksService: DatabricksService
) {
    @Value("\${sync-folder}") private lateinit var syncFolder: String
    @Value("\${volume}") private lateinit var volume: String
    @Value("\${folder}") private lateinit var folder: String

    fun syncFiles() {
        val syncHistoryId = insertSyncHistory()

        for (file in File(syncFolder).listFiles()) {
            if (file.isDirectory) continue
            val extension = file.extension
            if (extension != "xml" && extension != "zip") continue

            val name = file.name
            val fileHistory = findByName(name)
            if (fileHistory == null) {
                databricksService.uploadFile("$volume/$folder/$name", file)
                insertFileHistory(name, syncHistoryId)
                println("Uploaded File $name")
            } else {
                updateValidation(fileHistory.id, syncHistoryId)
                println("Updated info")
            }
        }
    }

    private fun insertFileHistory(name: String, syncHistoryId: Long) {
        val sql = "INSERT INTO file_history (name, added_at, last_validated_at) VALUES (:filename, $syncHistoryId, $syncHistoryId)"
        val params = MapSqlParameterSource()
            .addValue("filename", name, Types.VARCHAR)
        template.update(sql, params)
    }

    private fun updateValidation(id: Long, syncHistoryId: Long) {
        val sql = "UPDATE file_history SET last_validated_at = $syncHistoryId WHERE id = $id"
        template.update(sql, MapSqlParameterSource())
    }

    private fun insertSyncHistory(): Long {
        val sql = "insert into sync_history (id) values (default) returning id;"
        return template.queryForObject(sql, MapSqlParameterSource()) { rs, _ ->
            rs.getLong("id")
        }!!
    }

    private fun findByName(filename: String): FileHistoryDto? {
        val sql = "SELECT * FROM file_history WHERE name = :name"
        val params = MapSqlParameterSource()
            .addValue("name", filename, Types.VARCHAR)

        return try {
            template.queryForObject(sql, params) { rs, _ ->
                FileHistoryDto(
                    id = rs.getLong("id"),
                    name = rs.getString("name"),
                    addedAt = rs.getLong("added_at"),
                    lastValidatedAt = rs.getLong("last_validated_at")
                )
            }
        } catch (_: EmptyResultDataAccessException) {
            null
        }
    }
}