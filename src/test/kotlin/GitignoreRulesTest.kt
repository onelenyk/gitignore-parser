import dev.onelenyk.GitignoreRules
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GitignoreRulesTest {
    @Test
    fun testRegexConversion() {
        val gitignoreRules =
            GitignoreRules(
                listOf("*.log", "*.tmp"),
                customRules = listOf(".*\\.idea(/|\$)", ".*\\.jar\$"),
                key = "/",
            )
        // Test a standard pattern
        assertTrue(gitignoreRules.excludingPattern("test.log") != null, "test.log should be excluded")
        // Test a custom regex pattern
        assertTrue(
            gitignoreRules.excludingPattern("project.idea") != null,
            "project.idea should be excluded by custom rule",
        )
        // Test a pattern that should not be excluded
        assertFalse(gitignoreRules.excludingPattern("test.txt") != null, "test.txt should not be excluded")
    }

    @Test
    fun testEmptyRules() {
        val emptyRules = GitignoreRules(emptyList(), key = "/")
        assertNull(emptyRules.excludingPattern("file.txt"), "No pattern should match as rules are empty")
    }

    @Test
    fun testRuleIgnoring() {
        val rulesWithComments = listOf("# This is a comment", "\n", "*.tmp")
        val gitignoreRules = GitignoreRules(rulesWithComments, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("test.tmp"), "test.tmp should be excluded")
    }

    @Test
    fun testNegationPatternIgnoring() {
        val rulesWithNegation = listOf("!*.tmp", "*.tmp")
        val gitignoreRules = GitignoreRules(rulesWithNegation, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("test.tmp"), "test.tmp should be excluded despite negation")
    }

    @Test
    fun testCustomRulesApplication() {
        val customRules = GitignoreRules(emptyList(), listOf(".*\\.custom\$"), key = "/")
        assertNotNull(customRules.excludingPattern("file.custom"), "file.custom should be excluded by custom rule")
    }

    @Test
    fun testCachingEfficiency() {
        val rules = listOf("*.cache")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        val firstCall = gitignoreRules.excludingPattern("lenyk/test.cache")
        val secondCall = gitignoreRules.excludingPattern("lenyk/test.cache")
        assertEquals(firstCall, secondCall, "Repeated calls should retrieve the same cached regex")
    }

    @Test
    fun testComplexPatterns() {
        val complexRules = listOf("**/*.complex")
        val gitignoreRules = GitignoreRules(complexRules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("dir/subdir/file.complex"), "Complex pattern should exclude file")
    }

    @Test
    fun testMultipleWildcards() {
        val rules = listOf("*/*.log")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("dir/file.log"), "Multiple wildcards pattern should exclude file")
    }

    @Test
    fun testNestedWildcards() {
        val rules = listOf("**/logs/*.log")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("dir/logs/file.log"), "Nested wildcards pattern should exclude file")
    }

    @Test
    fun testMixedWildcardsAndCharacters() {
        val rules = listOf("*.log.*")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("file.log.txt"), "Mixed wildcards and characters pattern should exclude file")
    }

    @Test
    fun testCharacterClasses() {
        val rules = listOf("*.[tj]s")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("file.ts"), "Character classes pattern should exclude file")
    }

    @Test
    fun testEscapedCharacters() {
        val rules = listOf("\\*.log")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNull(gitignoreRules.excludingPattern("file.log"), "Escaped characters pattern should not exclude file")
    }

    @Test
    fun testDirectories() {
        val rules = listOf("logs/")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("logs/file.txt"), "Directories pattern should exclude file in directory")
    }

    @Test
    fun testNegatedCharacterClasses() {
        val rules = listOf("*.[^tj]s")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("file.cs"), "Negated character classes pattern should exclude file")
    }

    @Test
    fun testComplexBraces() {
        val rules = listOf("{*.txt,*.log}")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("file.txt"), "Complex braces pattern should exclude file")
    }

    @Test
    fun testLeadingDirectory() {
        val rules = listOf("/logs/*.log")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("/logs/file.log"), "Leading directory pattern should exclude file")
    }

    @Test
    fun testLeadingDirectory2() {
        val rules = listOf("/logs/*.log")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("logs/file.log"), "Leading directory pattern should exclude file")
    }

    @Test
    fun testDirectoryAndFileCombination() {
        val rules = listOf("logs/*.log")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("logs/file.log"), "Directory and file combination pattern should exclude file")
    }

    @Test
    fun testBuildExcluding() {
        val rules = listOf("build/")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        val firstCall = gitignoreRules.excludingPattern("build/reports/tests/test/packages/default-package.html")

        assertNotNull(firstCall, "Repeated calls should retrieve the same cached regex")
    }

    @Test
    fun testHiddenFilesAndDirectories() {
        val rules = listOf(".env", ".DS_Store")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern(".env"), ".env should be excluded")
        assertNotNull(gitignoreRules.excludingPattern(".DS_Store"), ".DS_Store should be excluded")
    }

    @Test
    fun testSpecificFileExclusion() {
        val rules = listOf("secrets.yaml")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("configs/secrets.yaml"), "secrets.yaml should be excluded")
    }

    @Test
    fun testComplexPathPatterns() {
        val rules = listOf("assets/images/**/*.png")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("assets/images/icons/home.png"), "PNG files in assets/images should be excluded")
    }

    // /

    @Test
    fun testRootLevelSpecificFile() {
        val rules = listOf("/todo.md")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("todo.md"), "Root level todo.md should be excluded")
    }

    // 5. Directory Exclusion with Exception
//    @Test
//    fun testDirectoryExclusionWithException() {
//        val rules = listOf("logs/", "!logs/important.log")
//        val gitignoreRules = GitignoreRules(rules, key = "/")
//        assertNull(gitignoreRules.excludingPattern("logs/important.log"), "important.log in logs should not be excluded")
//    }

    // 6. Wildcard in Middle of Pattern
    @Test
    fun testWildcardInMiddleOfPattern() {
        val rules = listOf("doc/*.md")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("doc/readme.md"), "Markdown files in doc should be excluded")
    }

    // 7. Case Sensitivity Test
    @Test
    fun testCaseSensitivity() {
        val rules = listOf("README.md")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNull(gitignoreRules.excludingPattern("readme.md"), "readme.md should not be excluded due to case sensitivity")
    }

    // 8. Pattern with Spaces
    @Test
    fun testPatternWithSpaces() {
        val rules = listOf("notes/Meeting Notes.txt")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(gitignoreRules.excludingPattern("notes/Meeting Notes.txt"), "Meeting Notes.txt in notes should be excluded")
    }

    // 9. Deeply Nested Pattern
    @Test
    fun testDeeplyNestedPattern() {
        val rules = listOf("src/main/java/com/example/utils/")
        val gitignoreRules = GitignoreRules(rules, key = "/")
        assertNotNull(
            gitignoreRules.excludingPattern("src/main/java/com/example/utils/Logger.java"),
            "Files in com/example/utils should be excluded",
        )
    }
}
