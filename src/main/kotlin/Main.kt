import java.nio.file.Paths

fun main() {
    val projectRoot = Paths.get("").toAbsolutePath()
    val gitignore = projectRoot.resolve(".gitignore").toString()
    val gitIgnoreLoader = GitIgnoreParser(gitignore)
    val fileProcessor = FileProcessor(projectRoot, gitIgnoreLoader)
    fileProcessor.processFiles()
    fileProcessor.printSummary()
}

/*
fun onelenyk() {
    val customRules = listOf(".git", ".idea")
    val projectRoot = Paths.get("/Users/lenyk/AndroidStudioProjects/onelenykco")
    val gitIgnoreLoader = GitIgnoreParser(projectRoot.resolve(".gitignore").toString(), customRules = customRules)

    println("Processing files in project directory... ${projectRoot}")

    processFiles(projectRoot, gitIgnoreLoader)
}
fun bandana() {
    val customRules = listOf(".git", ".idea")
    val projectRoot = Paths.get("/Users/lenyk/AndroidStudioProjects/thebandanaco")
    val gitIgnoreLoader = GitIgnoreParser(projectRoot.resolve(".gitignore").toString(), customRules = customRules)

    println("Processing files in project directory... ${projectRoot}")

    processFiles(projectRoot, gitIgnoreLoader)
}
fun vanongodriver() {
    val customRules = listOf(".git", ".idea")
    val projectRoot = Paths.get("/Users/lenyk/projects/android-driver")

    val gitIgnoreLoader = GitIgnoreParser(
        projectRoot.resolve(".gitignore").toString(),
        customRules = customRules
    )

    println("Processing files in project directory... ${projectRoot}")

    processFiles(projectRoot, gitIgnoreLoader)
}*/
