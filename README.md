# GitIgnoreParser
[![](https://jitpack.io/v/onelenyk/gitignore-parser.svg)](https://jitpack.io/#onelenyk/gitignore-parser)

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

Add the JitPack repository to your `build.gradle.kts`:

```gradle
allprojects {
    repositories {
        ...
        maven(url = "https://jitpack.io")
    }
}
```

Add the dependency:

```gradle
dependencies {
    implementation ("com.github.onelenyk:gitignore-parser:v0.1.2")
}
```

## Usage

The GitIgnoreParser library is designed to be straightforward and easy to integrate into your Kotlin projects. Below is a step-by-step guide on how to use it effectively:

1. **Initialization**: Start by creating an instance of the `GitIgnoreParser`. You need to provide the path to your project's root directory and optionally, any custom rules as a list of strings.

    ```kotlin
    import dev.onelenyk.GitIgnoreParser
   
    val rootPath = Paths.get("/path/to/your/project").toAbsolutePath()
    val gitIgnoreParser = GitIgnoreParser(rootDirectory = rootPath)
    ```

2. **Parsing .gitignore File**: Invoke the `parseGitignore` method by passing the directory path that contains the `.gitignore` file. This will load and parse the rules defined in the file.

    ```kotlin
    gitIgnoreParser.parseGitignore(rootPath)
    ```

3. **Checking File Exclusion**: To determine if a specific file is excluded based on the `.gitignore` rules, use the `getRulesForDirectory` method to get the rules for the file's directory and then the `excludingPattern` method to check for exclusion.

    ```kotlin
    val filePath = rootPath.resolve("src/main/Example.kt")
    val rules = gitIgnoreParser.getRulesForDirectory(filePath.parent)
    val isExcluded = rules?.excludingPattern(filePath.fileName.toString()) != null
    println("Is the file excluded: $isExcluded")
    ```

4. **Processing Entire Directory**: To process all files in a directory, you can utilize the `FileProcessor` class. It traverses the directory, checking each file against the `.gitignore` rules and compiling a list of included files.

    ```kotlin
    import dev.onelenyk.FileProcessor
   
    val fileProcessor = FileProcessor(rootDirectory = rootPath)
    fileProcessor.process()
    ```

5. **Getting Processing Summary**: After processing the files, you can get a summary of the operation, which includes details like the total number of files processed, the number of files included and excluded, and the patterns used.

    ```kotlin
    fileProcessor.report()
    ```

This Kotlin implementation of the GitIgnoreParser offers a user-friendly and efficient approach to handling `.gitignore` rules in Kotlin-based projects.


This Kotlin adaptation of the library offers a modern and concise approach, enhancing the user experience in
Kotlin-based projects.

## Contributions

Contributions are welcome! Please feel free to submit a pull request or open an issue for any improvements or bug fixes.

## Acknowledgements

- **onelenyk** - Initial work and maintenance.
- **ChatGPT by OpenAI** - Assisted in development by providing code insights, optimization strategies, and documentation
  support.

## License

This project is licensed under the [MIT License](LICENSE) - see the LICENSE file for details.
