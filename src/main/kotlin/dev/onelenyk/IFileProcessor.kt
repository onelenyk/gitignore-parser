package dev.onelenyk

import java.nio.file.Path

/**
 * Interface defining the behavior for processing files based on .gitignore rules.
 *
 * This interface outlines methods for traversing and processing files in a directory hierarchy,
 * applying .gitignore rules to determine which files should be included or excluded.
 */
interface IFileProcessor {
    /**
     * Processes all files in the root directory and its subdirectories.
     *
     * This method walks through the file tree, applying .gitignore rules to each file and directory
     * to determine whether they should be included or excluded.
     */
    fun process()

    /**
     * Generates and returns a summary report of the file processing.
     *
     * The report includes details such as the total number of files processed, the number of files
     * included and excluded, and the specific .gitignore patterns used for exclusion.
     *
     * @return A summary report of the file processing operation.
     */
    fun report()

    /**
     * Sets a custom file processing function.
     *
     * This method allows for the customization of how individual files are processed during the file
     * processing operation.
     *
     * @param function The custom function to be used for processing individual files.
     */
    fun setFileProcessorFunction(function: (Path) -> Unit)
}
