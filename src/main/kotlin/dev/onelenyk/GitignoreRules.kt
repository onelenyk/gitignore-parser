package dev.onelenyk

import dev.onelenyk.util.Logs.log

class GitignoreRules(
    val rawRules: List<String>,
    val customRules: List<String> = emptyList(),
    val key: String = "",
) : IGitignoreRules {
    private val regexCache = mutableMapOf<String, Regex>()
    private val regexRules: List<Regex> =
        rawRules.mapNotNull { processLine(it) }
            .map {
                val regex = getOrCompileRegex(it)
                regex
            }
            .toMutableList()
            .apply { addAll(addCustomRules()) }

    private fun addCustomRules(): List<Regex> {
        return customRules.map { rule ->
            val regexPattern = Regex(rule)
            log("Processed custom pattern: $rule as ${regexPattern.pattern}", isFine = true)
            return@map regexPattern
        }.apply {
            if (this.isNotEmpty()) {
                log("Custom rules added.")
            }
        }
    }

    private fun processLine(line: String): String? {
        val trimmedLine = line.trim()

        return when {
            trimmedLine.isBlank() || trimmedLine.isEmpty() -> {
                //   log("Processed pattern: $trimmedLine - ignored because of its empty", isFine = true)
                null
            }

            trimmedLine.startsWith("#") -> {
                //   log("Processed pattern: $trimmedLine - ignored because of its comments", isFine = true)
                null
            }

            trimmedLine.startsWith("!") -> {
                //   log("Processed pattern: $trimmedLine - ignored because of its negotiation pattern", isFine = true)
                null
            }

            else -> {
                trimmedLine
            }
        }
    }

    private fun convertGlobToRegex(pattern: String): Regex {
        // Immediately return regex for simple filename patterns
        when {
            isSimpleFilenamePattern(pattern) -> return Regex(handleSimpleFilenamePatterns(pattern))
            isRootedPattern(pattern) -> return Regex(handleRootedPatterns(pattern)) // drop the leading '/'
            // other specific cases...
        }

        var adjustedPattern = pattern

        // Modularized handling steps for other patterns
        adjustedPattern = handleDoubleAsterisk(adjustedPattern)
        adjustedPattern = handleNegatedCharacterClasses(adjustedPattern)
        adjustedPattern = escapeSpecialRegexCharacters(adjustedPattern)
        adjustedPattern = unescapeBraces(adjustedPattern)
        adjustedPattern = handleBraces(adjustedPattern)
        adjustedPattern = replaceGlobPatterns(adjustedPattern)
        adjustedPattern = handleDirectorySpecificPatterns(adjustedPattern)

        // Construct the final regex pattern
        return Regex(adjustedPattern)
    }

    private fun handleSimpleFilenamePatterns(pattern: String): String = ".*$pattern$"

    private fun isSimpleFilenamePattern(pattern: String): Boolean {
        // Check if the pattern is a filename without path or wildcards
        return !pattern.contains("/") && !pattern.contains("*") && !pattern.startsWith(".")
    }

    private fun handleDirectorySpecificPatterns(pattern: String): String {
        // Correctly handle directory-specific patterns
        return if (pattern.endsWith("/")) "^${pattern.dropLast(1)}/.*" else pattern
    }

    private fun handleDoubleAsterisk(pattern: String): String = if ("**" in pattern) pattern.replace("**", "§§") else pattern

    private fun handleNegatedCharacterClasses(pattern: String): String = if ("[^" in pattern) pattern.replace("[^", "UN1O") else pattern

    private fun escapeSpecialRegexCharacters(pattern: String): String = pattern.replace(Regex("[.^$+()|]")) { "\\${it.value}" }

    private fun unescapeBraces(pattern: String): String = pattern.replace(Regex("\\\\([{}])")) { it.groupValues[1] }

    private fun replaceGlobPatterns(pattern: String): String =
        pattern.replace("*", "[^/]*")
            .replace("?", "[^/]")
            .replace("§§", ".*")
            .replace("UN1O", "[^")

    private fun handleBraces(pattern: String): String {
        // This regex matches the content inside {...} and splits it by ','
        val braceRegex = Regex("\\{([^}]+)}")
        return if (braceRegex.containsMatchIn(pattern)) {
            pattern.replace(braceRegex) { matchResult ->
                val options = matchResult.groupValues[1].split(',')
                options.joinToString("|") { option ->
                    option.replace("*", "[^/]*") // Replace * for each option
                }.let { "($it)" }
            }
        } else {
            pattern
        }
    }

    private fun handleRootedPatterns(pattern: String): String {
        // Remove the leading '/' and then replace glob patterns
        var trimmedPattern = pattern.drop(1)

        // Modularized handling steps for other patterns
        trimmedPattern = handleDoubleAsterisk(trimmedPattern)
        trimmedPattern = handleNegatedCharacterClasses(trimmedPattern)
        trimmedPattern = escapeSpecialRegexCharacters(trimmedPattern)
        trimmedPattern = replaceGlobPatterns(trimmedPattern)

        return "^/?$trimmedPattern$"
    }

    private fun isRootedPattern(pattern: String): Boolean {
        return pattern.startsWith("/")
    }

    private fun getOrCompileRegex(pattern: String): Regex {
        return regexCache.getOrPut(pattern) {
            val regex = convertGlobToRegex(pattern)
            log("Processed pattern: $pattern as ${regex.pattern}", isFine = true)
            regex
        }
    }

    override fun excludingPattern(filePath: String): Regex? {
        return regexRules.firstOrNull { regex -> regex.matches(filePath) }
    }
}
