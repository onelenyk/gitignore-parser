import dev.onelenyk.GitIgnoreParser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GitIgnoreParserTest {
    private lateinit var gitignoreParser: GitIgnoreParser
    private lateinit var tempDir: Path
    private lateinit var tempGitignoreFile: Path
    private val expectedRules = listOf("*.log", "*.tmp")

    @BeforeEach
    fun setUp() {
        // Create a temporary directory
        tempDir = Files.createTempDirectory("gitignoreParserTest")

        gitignoreParser = GitIgnoreParser(rootDirectory = tempDir)

        // Create a .gitignore file in this directory
        tempGitignoreFile = tempDir.resolve(".gitignore")
        Files.write(tempGitignoreFile, expectedRules)
    }

    @Test
    fun testParseGitignore() {
        gitignoreParser.parseGitignore(tempGitignoreFile.parent)
        val rules = gitignoreParser.getRulesForDirectory(tempGitignoreFile.parent)
        assertNotNull(rules, "Rules should not be null")
        assertEquals(expectedRules, rules.rawRules, "Parsed rules should match the written content")
    }

    @AfterEach
    fun tearDown() {
        // Clean up: delete the temporary directory recursively
        tempDir.toFile().deleteRecursively()
    }
}
