package dev.onelenyk

import dev.onelenyk.util.FileProcessorAnalytics
import dev.onelenyk.util.Logs.log
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

class FileProcessor(
    private val rootDirectory: Path,
    val customRules: List<String> = emptyList(),
) {
    private val analytics = FileProcessorAnalytics()
    private val gitignoreParser = GitIgnoreParser(customRules = customRules, rootDirectory)

    // Customizable file processing function
    private var fileProcessorFunction: (Path) -> Unit = { path ->
    }

    fun process() {
        val includedPaths = mutableListOf<Path>()
        log("Starting file processing")

        Files.walkFileTree(
            rootDirectory,
            object : SimpleFileVisitor<Path>() {
                override fun preVisitDirectory(
                    dir: Path,
                    attrs: BasicFileAttributes,
                ): FileVisitResult {
                    try {
                        if (Files.exists(dir.resolve(".gitignore"))) {
                            log("Gitignore file detected in: ${dir.toAbsolutePath()} ", isInfo = true)
                            // Parse .gitignore if present in this directory
                            gitignoreParser.parseGitignore(dir)
                        }

                        // Check if the directory should be excluded based on parent rules
                        if (shouldExcludeDirectory(dir)) {
                            return FileVisitResult.SKIP_SUBTREE
                        } else {
                            val nicePath = gitignoreParser.path(dir)
                            analytics.onDirIncluded(nicePath)
                        }

                        return FileVisitResult.CONTINUE
                    } catch (e: IOException) {
                        log("Failed to visit directory: $dir (ERROR)", isError = true)

                        return FileVisitResult.SKIP_SUBTREE
                    }
                }

                override fun visitFile(
                    file: Path,
                    attrs: BasicFileAttributes,
                ): FileVisitResult {
                    try {
                        analytics.onFileAppear()
                        val nicePath = gitignoreParser.path(file.parent)
                        val parentRules = gitignoreParser.getRulesForDirectory(nicePath)
                        val niceFile = gitignoreParser.path(file)

                        if (parentRules != null) {
                            val pattern = Path.of(parentRules.key).relativize(niceFile)
                            val key = pattern.ifEmpty(nicePath.fileName)

                            val excludingPattern = parentRules.excludingPattern(key.toString())

                            if (excludingPattern == null) {
                                fileProcessorFunction(file)
                                analytics.onFileIncluded(niceFile)
                                includedPaths.add(niceFile)
                            } else {
                                analytics.onFileExcluded(niceFile, excludingPattern)
                            }
                        } else {
                            fileProcessorFunction(file)
                            analytics.onFileIncluded(niceFile)
                            includedPaths.add(niceFile)
                        }

                        return FileVisitResult.CONTINUE
                    } catch (e: IOException) {
                        log("Failed to visit: ${file.toAbsolutePath()} (ERROR)", isError = true)
                        return FileVisitResult.CONTINUE
                    }
                }

                override fun visitFileFailed(
                    file: Path,
                    exc: IOException,
                ): FileVisitResult {
                    log("Failed to visit: ${file.toAbsolutePath()} (ERROR)", isError = true)
                    return FileVisitResult.CONTINUE
                }

                private fun shouldExcludeDirectory(dir: Path): Boolean {
                    val nicePath = gitignoreParser.path(dir)
                    val parentRules = gitignoreParser.getRulesForDirectory(nicePath)

                    return if (parentRules != null) {
                        val pattern = Path.of(parentRules.key).relativize(nicePath)
                        val key = pattern.ifEmpty(nicePath.fileName)

                        val excludingPattern = parentRules.excludingPattern(key.toString())
                        excludingPattern?.let {
                            analytics.onDirExcluded(nicePath, excludingPattern)
                        }
                        excludingPattern != null
                    } else {
                        false
                    }
                }
            },
        )
    }

    private fun Path.ifEmpty(default: Path): Path {
        return if (this.isEmpty()) default else this
    }

    private fun Path.isEmpty(): Boolean {
        return this.toString().isEmpty()
    }

    fun report() = analytics.report()

    fun setFileProcessorFunction(function: (Path) -> Unit) {
        fileProcessorFunction = function
    }
}
