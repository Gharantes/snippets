package personal.dev.file_sync

data class FileHistoryDto(
    val id: Long,
    val addedAt: Long,
    val lastValidatedAt: Long,
    val name: String
)
