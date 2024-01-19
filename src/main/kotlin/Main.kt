import java.nio.file.Paths

// fun main() = run(arrayOf("/Users/lenyk/projects/android-driver"))
fun main() = run(arrayOf(""))

fun run(args: Array<String>) {
    val projectRoot = Paths.get(args[0]).toAbsolutePath()
    val gitignore = projectRoot.resolve(".gitignore").toString()
    val gitIgnoreLoader = GitIgnoreParser(gitignore)
    val fileProcessor = FileProcessor(projectRoot, gitIgnoreLoader)
    fileProcessor.processFiles()
    fileProcessor.printSummary()
}
