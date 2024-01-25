package dev.onelenyk

import java.nio.file.Paths

class Library {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val rootPath = Paths.get("/Users/lenyk/projects/android-driver").toAbsolutePath()

            //   val rootPath = Path.of("").toAbsolutePath()
            val fileProcessor =
                FileProcessor(
                    rootPath,
                    customRules =
                        listOf(
                            ".*\\.idea(/|\$)",
                            ".*\\.git(/|\$)",
                            ".*\\.jar\$",
                        ),
                )
            fileProcessor.process()

            println(fileProcessor.report())
        }
    }
}
