package dev.onelenyk

import java.nio.file.Path

/**
 * Interface defining the behavior for parsing .gitignore files.
 *
 * This interface provides methods to parse .gitignore files, retrieve rules for specific directories,
 * and manage file paths relative to the root directory of the parser.
 */
interface IGitIgnoreParser {
    /**
     * Parses the .gitignore file located in the specified directory.
     *
     * This method reads the .gitignore file, processes its rules, and stores them for later retrieval.
     * It handles both the rules defined in .gitignore and any custom rules provided.
     *
     * @param directory The directory where the .gitignore file is located.
     */
    fun parseGitignore(directory: Path)

    /**
     * Retrieves the set of .gitignore rules applicable to a specific directory.
     *
     * This method returns the rules that apply to the given directory, including those inherited from parent directories.
     *
     * @param directory The directory for which to retrieve the .gitignore rules.
     * @return The set of .gitignore rules for the directory, or null if no rules are applicable.
     */
    fun getRulesForDirectory(directory: Path): GitignoreRules?

    /**
     * Calculates the relative path of a directory or file from the root directory of the GitIgnoreParser.
     *
     * This method is used to determine the relative path used in .gitignore rules processing.
     *
     * @param directory The directory or file for which to calculate the relative path.
     * @return The relative path as a Path object.
     */
    fun path(directory: Path): Path
}
