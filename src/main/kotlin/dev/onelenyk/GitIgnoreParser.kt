package dev.onelenyk

import dev.onelenyk.util.Logs.log
import java.nio.file.Path

class GitIgnoreParser(
    val customRules: List<String> = emptyList(),
    val rootDirectory: Path,
) : IGitIgnoreParser {
    private val rulesMap = mutableMapOf<String, GitignoreRules>()

    override fun path(directory: Path): Path {
        val basePath = rootDirectory.parent
        val extractedPartSecond = basePath.relativize(directory)
        return extractedPartSecond
    }

    override fun parseGitignore(directory: Path) {
        try {
            val gitignoreFile = directory.resolve(".gitignore").toFile()
            if (gitignoreFile.exists()) {
                val rules = gitignoreFile.readLines()
                val key = path(directory).toString()
                rulesMap[key] = GitignoreRules(rawRules = rules, customRules = customRules, key = key)
                log("Loaded and parsed .gitignore successfully.")
            } else {
                log("Error loading .gitignore file", isError = true)
            }
        } catch (e: Exception) {
            log("Error loading .gitignore file: ${e.message}", isError = true)
            throw e
        }
    }

    override fun getRulesForDirectory(directory: Path): GitignoreRules? {
        var currentDir: Path? = directory
        while (currentDir != null) {
            val key = currentDir.toString()
            val rules = rulesMap[key]
            if (rules != null) {
                return rules
            }
            currentDir = currentDir.parent
        }
        return null // No rules found up to the root
    }
}
