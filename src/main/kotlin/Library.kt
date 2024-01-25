import java.nio.file.Path

class Library {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val rootPath = Path.of("/Users/lenyk/projects/android-driver").toAbsolutePath()

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
