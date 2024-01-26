package dev.onelenyk

/**
 * Interface defining the behavior for interpreting and applying .gitignore rules.
 *
 * This interface is responsible for determining if specific files or directories
 * are excluded based on the .gitignore rules.
 */
interface IGitignoreRules {
    /**
     * Determines if the provided file path is excluded by the .gitignore rules.
     *
     * This method checks the given file path against the compiled regex patterns
     * derived from the .gitignore rules to decide if the file should be excluded.
     *
     * @param filePath The file path to check against the .gitignore rules.
     * @return A Regex object if the file path is excluded by any of the rules, null otherwise.
     */
    fun excludingPattern(filePath: String): Regex?
}
