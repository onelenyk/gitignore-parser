# GitIgnoreParser

GitIgnoreParser is an advanced Kotlin library designed to parse `.gitignore` files and determine file exclusions based
on gitignore specifications with efficiency and precision. This tool is particularly useful for developers looking to
programmatically apply `.gitignore` rules in their Kotlin-based projects.

## Features

- **Kotlin-Friendly**: Designed with Kotlin's modern language features in mind for a seamless integration.
- **Efficient Parsing**: Converts `.gitignore` glob patterns to regex for precise and fast matching.
- **Nested `.gitignore` Support**: Handles `.gitignore` files in subdirectories, ensuring comprehensive rule
  application.
- **Directory Specific Rules**: Accurately applies rules specific to directories, adhering to `.gitignore` standards.
- **Optimized for Large Projects**: Ideal for use in large-scale projects with extensive file structures, ensuring
  performance.
- **Advanced Logging**: Integrates sophisticated logging mechanisms for enhanced debugging and operational tracking.

## Installation

### Using JitPack

Add the JitPack repository to your build file:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency:

```gradle
dependencies {
   implementation 'com.github.YourUsername:GitIgnoreParser:Tag'
}
```

## Usage

The GitIgnoreParser library is seamlessly integrable into your Kotlin projects. Here’s how to use it:

1. **Initialize the Parser**: Create an instance of `GitIgnoreParser` by passing the path to your `.gitignore` file.

    ```kotlin
    val gitIgnoreFilePath = "/path/to/your/.gitignore"
    val parser = GitIgnoreParser(gitIgnoreFilePath)
    ```

2. **Check File Exclusion**: To determine if a specific file is excluded by the `.gitignore` rules, use
   the `isExcludedByGitignore` method. Provide the relative path of the file to this method.

    ```kotlin
    val relativeFilePath = "src/main/Example.kt"
    val isExcluded = parser.isExcludedByGitignore(relativeFilePath)
    println("Is the file excluded: $isExcluded")
    ```

3. **Get Exclusion Pattern**: To find out which specific `.gitignore` pattern is causing a file to be excluded, use
   the `isExcludedByGitignoreWithPattern` method. This returns the matching pattern, if any.

    ```kotlin
    val exclusionPattern = parser.isExcludedByGitignoreWithPattern(relativeFilePath)
    exclusionPattern?.let {
        println("Excluded by pattern: $it")
    } ?: println("File is not excluded.")
    ```

4. **Process Files in a Directory**: To process all files in a directory, utilize the `FileProcessor` class. It helps in
   traversing the directory, checking each file against the `.gitignore` rules, and compiling a list of included files.

    ```kotlin
    val projectRoot = Paths.get("/path/to/your/project")
    val fileProcessor = FileProcessor(projectRoot, parser)
    val includedFiles = fileProcessor.processFiles()
    ```

5. **Print Summary**: You can print a summary of the operation after processing the files, including the total files
   processed, files included, and patterns used.

    ```kotlin
    fileProcessor.printSummary()
    ```

This Kotlin adaptation of the library offers a modern and concise approach, enhancing the user experience in
Kotlin-based projects.

## Sample Console Output

To give you a better idea of how the GitIgnoreParser works in practice, here is a sample of the console output you can
expect. This output demonstrates the library's process of evaluating files against `.gitignore` rules and provides
insights into its operation.

### Sample Log:

```plaintext
ℹ️ Initializing GitIgnoreParser
🔍 Processed pattern: .gradle as (^|/.*/)\.gradle
🔍 Processed pattern: build/ as .*build/(|.*/.*)
🔍 Processed pattern: .idea/modules.xml as (^|/.*/)\.idea/modules\.xml
🔍 Processed pattern: .idea/jarRepositories.xml as (^|/.*/)\.idea/jarRepositories\.xml
🔍 Processed pattern: .idea/compiler.xml as (^|/.*/)\.idea/compiler\.xml
🔍 Processed pattern: .idea/libraries/ as .*\.idea/libraries/(|.*/.*)
🔍 Processed pattern: *.iws as (^|/.*/).*\.iws
🔍 Processed pattern: *.iml as (^|/.*/).*\.iml
🔍 Processed pattern: *.ipr as (^|/.*/).*\.ipr
🔍 Processed pattern: out/ as .*out/(|.*/.*)
🔍 Processed pattern: .apt_generated as (^|/.*/)\.apt_generated
🔍 Processed pattern: .classpath as (^|/.*/)\.classpath
🔍 Processed pattern: .factorypath as (^|/.*/)\.factorypath
🔍 Processed pattern: .project as (^|/.*/)\.project
🔍 Processed pattern: .settings as (^|/.*/)\.settings
🔍 Processed pattern: .springBeans as (^|/.*/)\.springBeans
🔍 Processed pattern: .sts4-cache as (^|/.*/)\.sts4-cache
🔍 Processed pattern: bin/ as .*bin/(|.*/.*)
🔍 Processed pattern: /nbproject/private/ as .*^/nbproject/private/(|.*/.*)
🔍 Processed pattern: /nbbuild/ as .*^/nbbuild/(|.*/.*)
🔍 Processed pattern: /dist/ as .*^/dist/(|.*/.*)
🔍 Processed pattern: /nbdist/ as .*^/nbdist/(|.*/.*)
🔍 Processed pattern: /.nb-gradle/ as .*^/\.nb-gradle/(|.*/.*)
🔍 Processed pattern: .vscode/ as .*\.vscode/(|.*/.*)
🔍 Processed pattern: .DS_Store as (^|/.*/)\.DS_Store
ℹ️ Loaded and parsed .gitignore successfully.
🔍 Processed pattern: .*\.idea(/|$) as .*\.idea(/|$)
🔍 Processed pattern: .*\.git(/|$) as .*\.git(/|$)
🔍 Processed pattern: .*\.jar$ as .*\.jar$
ℹ️ Custom rules added.
ℹ️ Initialization complete. Total patterns loaded: 28
🔍 Starting file processing
➕ Included:  (DIRECTORY)
➕ Included: /Users/lenyk/IdeaProjects/gitignore-parser/readme.md (FILE)
➕ Included: gradle (DIRECTORY)
➕ Included: gradle/wrapper (DIRECTORY)
➖ Excluded: /Users/lenyk/IdeaProjects/gitignore-parser/gradle/wrapper/gradle-wrapper.jar (FILE) by patter .*\.jar$
➕ Included: /Users/lenyk/IdeaProjects/gitignore-parser/gradle/wrapper/gradle-wrapper.properties (FILE)
➕ Included: /Users/lenyk/IdeaProjects/gitignore-parser/gradlew (FILE)
➕ Included: /Users/lenyk/IdeaProjects/gitignore-parser/.gitignore (FILE)
➕ Included: /Users/lenyk/IdeaProjects/gitignore-parser/build.gradle.kts (FILE)
➖ Excluded: .gradle (DIRECTORY) by patter (^|/.*/)\.gradle
➕ Included: /Users/lenyk/IdeaProjects/gitignore-parser/settings.gradle.kts (FILE)
➕ Included: /Users/lenyk/IdeaProjects/gitignore-parser/gradle.properties (FILE)
➕ Included: build (DIRECTORY)
➕ Included: build/classes (DIRECTORY)
➖ Excluded: build/classes/kotlin (DIRECTORY) by patter .*build/(|.*/.*)
➕ Included: build/kotlin (DIRECTORY)
➖ Excluded: build/kotlin/sessions (DIRECTORY) by patter .*build/(|.*/.*)
➖ Excluded: build/kotlin/compileKotlin (DIRECTORY) by patter .*build/(|.*/.*)
➖ Excluded: .git (DIRECTORY) by patter .*\.git(/|$)
➕ Included: /Users/lenyk/IdeaProjects/gitignore-parser/gradlew.bat (FILE)
➖ Excluded: .idea (DIRECTORY) by patter .*\.idea(/|$)
➕ Included: src (DIRECTORY)
➕ Included: src/test (DIRECTORY)
➕ Included: src/test/resources (DIRECTORY)
➕ Included: src/test/kotlin (DIRECTORY)
➕ Included: src/build (DIRECTORY)
➕ Included: src/build/snapshot (DIRECTORY)
➖ Excluded: src/build/snapshot/kotlin (DIRECTORY) by patter .*build/(|.*/.*)
➕ Included: src/build/classes (DIRECTORY)
➖ Excluded: src/build/classes/kotlin (DIRECTORY) by patter .*build/(|.*/.*)
➕ Included: src/build/publications (DIRECTORY)
➖ Excluded: src/build/publications/mavenJava (DIRECTORY) by patter .*build/(|.*/.*)
➕ Included: src/build/kotlin (DIRECTORY)
➖ Excluded: /Users/lenyk/IdeaProjects/gitignore-parser/src/build/kotlin/gitignoreparser010jar-classes.txt (FILE) by patter .*build/(|.*/.*)
➖ Excluded: src/build/kotlin/sessions (DIRECTORY) by patter .*build/(|.*/.*)
➖ Excluded: src/build/kotlin/compileKotlin (DIRECTORY) by patter .*build/(|.*/.*)
➕ Included: src/build/libs (DIRECTORY)
➖ Excluded: /Users/lenyk/IdeaProjects/gitignore-parser/src/build/libs/gitignore-parser-0.1.0.jar (FILE) by patter .*build/(|.*/.*)
➕ Included: src/build/intermediates (DIRECTORY)
➖ Excluded: src/build/intermediates/ktLint (DIRECTORY) by patter .*build/(|.*/.*)
➕ Included: src/build/tmp (DIRECTORY)
➖ Excluded: src/build/tmp/jar (DIRECTORY) by patter .*build/(|.*/.*)
➖ Excluded: src/build/tmp/publishMavenJavaPublicationToMavenLocal (DIRECTORY) by patter .*build/(|.*/.*)
➖ Excluded: src/build/tmp/shadowJar (DIRECTORY) by patter .*build/(|.*/.*)
➕ Included: src/build/reports (DIRECTORY)
➖ Excluded: src/build/reports/ktlint (DIRECTORY) by patter .*build/(|.*/.*)
➕ Included: src/main (DIRECTORY)
➕ Included: src/main/resources (DIRECTORY)
➕ Included: src/main/kotlin (DIRECTORY)
➕ Included: /Users/lenyk/IdeaProjects/gitignore-parser/src/main/kotlin/FileProcessor.kt (FILE)
➕ Included: /Users/lenyk/IdeaProjects/gitignore-parser/src/main/kotlin/Main.kt (FILE)
➕ Included: /Users/lenyk/IdeaProjects/gitignore-parser/src/main/kotlin/GitIgnoreParser.kt (FILE)
🔍 File processing completed
🔍 Summary:
ℹ️ Total Items Processed: 52
ℹ️ Total Files: 14
ℹ️ Total Directories: 38
ℹ️ Files/Directories Skipped: 3
ℹ️ Files selected: 11
💼 Patterns Used: ▓▓.*\.jar$▓▓, ▓▓(^|/.*/)\.gradle▓▓, ▓▓.*build/(|.*/.*)▓▓, ▓▓.*\.git(/|$)▓▓, ▓▓.*\.idea(/|$)▓▓
```

This log shows the detailed process of how the GitIgnoreParser assesses each file, including any `.gitignore` patterns
that apply, and the final decision on whether each file is ignored or included.

Note: The actual output may vary based on your project's `.gitignore` file and the specific files being processed.

## Contributions

Contributions are welcome! Please feel free to submit a pull request or open an issue for any improvements or bug fixes.

## Acknowledgements

- **onelenyk** - Initial work and maintenance.
- **ChatGPT by OpenAI** - Assisted in development by providing code insights, optimization strategies, and documentation
  support.

## License

This project is licensed under the [MIT License](LICENSE) - see the LICENSE file for details.
