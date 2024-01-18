import java.io.File

class GitIgnoreParser(
    private val gitIgnoreFilePath: String,
    private val customRules: List<String> = listOf(".git", ".idea", "*.jar"),
    private val enableLogging: Boolean = true,
) {
    private val regexPatterns = mutableListOf<Regex>()

    init {
        log("Initializing GitIgnoreParser")
        loadAndParseGitIgnore()
        addCustomRules()
        log("Initialization complete. Total patterns loaded: ${regexPatterns.size}")
    }

    private fun addCustomRules() {
        customRules.forEach { rule ->
            processLine(rule)
        }
        log("Custom rules added.")
    }

    private fun loadAndParseGitIgnore() {
        try {
            File(gitIgnoreFilePath).forEachLine { line ->
                processLine(line)
            }
            log("Loaded and parsed .gitignore successfully.")
        } catch (e: Exception) {
            log("Error loading .gitignore file: ${e.message}", isError = true)
        }
    }

    private fun processLine(line: String) {
        val trimmedLine = line.trim()
        if (trimmedLine.isNotEmpty() && !trimmedLine.startsWith("#")) {
            val regexPattern = convertGlobToRegex(trimmedLine)
            regexPatterns.add(regexPattern)
            log("Processed pattern: $trimmedLine", isFine = true)
        }
    }

    private fun convertGlobToRegex(pattern: String): Regex {
        var regex =
            pattern
                .trimStart('/')
                .replace(".", "\\.")
                .replace("*", ".*")
                .replace("?", ".")

        if (pattern.startsWith("/")) {
            regex = "^$regex"
        }

        if (pattern.endsWith("/")) {
            regex += "(|.*/.*)"
        } else {
            regex = "(^|/.*/)$regex"
        }

        return Regex(regex)
    }

    fun isExcludedByGitignore(relativePath: String): Boolean {
        return regexPatterns.any { it.containsMatchIn(relativePath) }
    }

    fun isExcludedByGitignoreWithPattern(relativePath: String): String? {
        return regexPatterns.firstOrNull { regex ->
            regex.containsMatchIn(relativePath)
        }?.pattern
    }

    private fun log(
        message: String,
        isError: Boolean = false,
        isFine: Boolean = false,
    ) {
        if (!enableLogging) return
        val emoji =
            when {
                isError -> "❌"
                isFine -> "🔍"
                else -> "ℹ️"
            }
        println("$emoji $message")
    }
}
