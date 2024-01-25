package dev.onelenyk.util

import dev.onelenyk.util.Logs.log
import java.nio.file.Path

class FileProcessorAnalytics {
    var totalFileCount: Int = 0
        private set
    var excludedFileCount: Int = 0
        private set
    var includedFileCount: Int = 0
        private set
    var excludedDirectoryCount: Int = 0
        private set
    var includedDirectoryCount: Int = 0
        private set
    var usedPatterns = mutableSetOf<Regex>()
        private set

    fun onFileAppear() {
        totalFileCount++
    }

    fun onFileIncluded(directory: Path) {
        includedFileCount++
        log("Included: $directory (FILE)", prefix = "➕")
    }

    fun onFileExcluded(
        directory: Path,
        excludingPattern: Regex,
    ) {
        excludedFileCount++
        onPatternUsed(excludingPattern)
        log("Excluded: $directory (FILE) by pattern $excludingPattern", prefix = "➖")
    }

    fun onDirExcluded(
        directory: Path,
        excludingPattern: Regex,
    ) {
        excludedDirectoryCount++
        onPatternUsed(excludingPattern)
        log("Excluded: $directory (DIRECTORY) by pattern $excludingPattern", prefix = "➖")
    }

    fun onDirIncluded(directory: Path) {
        includedDirectoryCount++
        log("Included: $directory (DIRECTORY)", prefix = "➕")
    }

    private fun onPatternUsed(pattern: Regex) {
        usedPatterns.add(pattern)
    }

    fun report() {
        log("Summary:")
        log("Total Items Processed: $totalFileCount", isInfo = true)
        log("Directories included: $includedDirectoryCount", isInfo = true)
        log("Directories excluded: $excludedDirectoryCount", isInfo = true)
        log("Files included: $includedFileCount", isInfo = true)
        log("Files excluded: $excludedFileCount", isInfo = true)
        log("Patterns Used: ${usedPatterns.joinToString { "▓▓$it▓▓" }}", prefix = "\uD83D\uDCBC")
    }

    // Additional metrics and methods can be added as needed
}
