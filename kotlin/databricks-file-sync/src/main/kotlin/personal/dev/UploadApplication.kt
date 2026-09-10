package personal.dev

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import personal.dev.databricks.DatabricksDetailsService
import personal.dev.databricks.DatabricksService
import personal.dev.file_sync.FileSyncService
import java.io.File
import kotlin.system.exitProcess

@SpringBootApplication
open class UploadApplication (
    private val databricksService: DatabricksService,
    private val fileSyncService: FileSyncService
) {
    @Bean
    open fun runOnce(): CommandLineRunner = CommandLineRunner {
        fileSyncService.syncFiles()
        exitProcess(0)
    }
}
fun main(args: Array<String>) {
    runApplication<UploadApplication>(*args)
}
