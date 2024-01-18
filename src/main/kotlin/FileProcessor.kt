import java.io.File
import java.io.IOException
import java.nio.file.FileVisitOption
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.EnumSet

class FileProcessor(private val projectRoot: Path, private val gitIgnoreLoader: GitIgnoreParser) {
    private var fileCount = 0
    private var directoryCount = 0
    private var skippedFileCount = 0
    private var skippedDirectoryCount = 0
    private var totalItemCount = 0
    private val usedPatterns = mutableSetOf<String>()

    fun processFiles() {
        log("Starting file processing")
        Files.walkFileTree(projectRoot, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Int.MAX_VALUE, FileVisitor())
        log("File processing completed")
    }

    private inner class FileVisitor : SimpleFileVisitor<Path>() {
        override fun visitFile(
            file: Path?,
            attrs: BasicFileAttributes?,
        ): FileVisitResult {
            totalItemCount++
            file?.let { filePath ->
                fileCount++
                if (!attrs?.isDirectory!!) {
                    val relativePath = projectRoot.relativize(filePath).toString().replace(File.separatorChar, '/')

                    val pattern = gitIgnoreLoader.isExcludedByGitignoreWithPattern(relativePath)

                    if (pattern == null) {
                        log("Included: $relativePath (FILE)", prefix = "➕")
                    } else {
                        pattern.let { usedPatterns.add(it) }

                        skippedFileCount++
                        log("Excluded: $relativePath (FILE)", prefix = "➖")
                    }
                }
            }
            return FileVisitResult.CONTINUE
        }

        override fun visitFileFailed(
            file: Path?,
            exc: IOException?,
        ): FileVisitResult {
            file?.let { filePath ->
                log("Failed to visit: ${filePath.toAbsolutePath()} (ERROR)", isError = true)
            }
            return FileVisitResult.CONTINUE
        }

        override fun preVisitDirectory(
            dir: Path?,
            attrs: BasicFileAttributes?,
        ): FileVisitResult {
            totalItemCount++
            dir?.let { dirPath ->
                directoryCount++
                val relativePath = projectRoot.relativize(dirPath).toString().replace(File.separatorChar, '/')
                val pattern = gitIgnoreLoader.isExcludedByGitignoreWithPattern(relativePath)

                if (pattern == null) {
                    log("Included: $relativePath (DIRECTORY)", prefix = "➕")
                } else {
                    pattern.let { usedPatterns.add(it) }
                    skippedDirectoryCount++
                    log("Excluded: $relativePath (DIRECTORY)", prefix = "➖")
                    return FileVisitResult.SKIP_SUBTREE
                }
            }
            return FileVisitResult.CONTINUE
        }
    }

    private fun log(
        message: String,
        isError: Boolean = false,
        isInfo: Boolean = false,
        prefix: String? = null,
    ) {
        val emoji =
            when {
                isError -> "❌"
                isInfo -> "ℹ️"
                prefix != null -> prefix
                else -> "🔍"
            }
        println("$emoji $message")
    }

    fun printSummary() {
        log("Summary:")
        log("Total Items Processed: $totalItemCount", isInfo = true)
        log("Total Files: $fileCount", isInfo = true)
        log("Total Directories: $directoryCount", isInfo = true)
        log("Files/Directories Skipped: $skippedFileCount", isInfo = true)
        log("Patterns Used: ${usedPatterns.joinToString { "▓▓$it▓▓" }}", prefix = "\uD83D\uDCBC")
    }
}
