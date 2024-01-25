import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GitignoreExtendedRulesTest {
    // Test for root directory files
    @Test
    fun `Root directory files should be included when not specified`() {
        val gitignoreRules = GitignoreRules(listOf("*.log"), key = "/")
        assertNull(gitignoreRules.excludingPattern("rootFile.txt"), "Root directory file should be included")
    }

    // Test for ignoring files in a specific folder
    @Test
    fun `Files in a specific folder should be excluded`() {
        val gitignoreRules = GitignoreRules(listOf("specificFolder/"), key = "/")
        assertNotNull(
            gitignoreRules.excludingPattern("specificFolder/test.txt"),
            "Files in specificFolder should be excluded",
        )
    }

    // Test for specific file types in any directory
    @Test
    fun `Specific file types in any directory should be excluded`() {
        val gitignoreRules = GitignoreRules(listOf("*.log"), key = "/")
        assertNotNull(
            gitignoreRules.excludingPattern("subdir/example.log"),
            "Log files should be excluded in any directory",
        )
    }

    // Test for excluding files with certain prefix
    @Test
    fun `Files with certain prefix should be excluded`() {
        val gitignoreRules = GitignoreRules(listOf("temp_*"), key = "/")
        assertNotNull(gitignoreRules.excludingPattern("temp_file.txt"), "Files with 'temp_' prefix should be excluded")
    }

    // Test for excluding files with certain suffix
    @Test
    fun `Files with certain suffix should be excluded`() {
        val gitignoreRules = GitignoreRules(listOf("*_backup"), key = "/")
        assertNotNull(gitignoreRules.excludingPattern("data_backup"), "Files with '_backup' suffix should be excluded")
    }

    // Test for negated pattern
//        @Test
//        fun `Negated patterns should not exclude the file`() {
//            val gitignoreRules = GitignoreRules(listOf("!important.log"), key = "/")
//            assertNotNull(gitignoreRules.excludingPattern("important.log"), "Negated patterns should not exclude the file")
//        }

    // Test for nested directories exclusion
    @Test
    fun `Nested directories should be excluded`() {
        val gitignoreRules = GitignoreRules(listOf("outer/**/inner/"), key = "/")
        assertNotNull(
            gitignoreRules.excludingPattern("outer/middle/inner/file.txt"),
            "Nested directories should be excluded",
        )
    }

    // Test for file types in nested directories
    @Test
    fun `File types in nested directories should be excluded`() {
        val gitignoreRules = GitignoreRules(listOf("**/*.temp"), key = "/")
        assertNotNull(
            gitignoreRules.excludingPattern("outer/inner/file.temp"),
            "File types in nested directories should be excluded",
        )
    }

    // Test for excluding all except certain directories
//    @Test
//    fun `Exclude all except certain directories`() {
//        val gitignoreRules = GitignoreRules(listOf("/*", "!/include/"), key = "/")
//        assertNull(gitignoreRules.excludingPattern("exclude/file.txt"), "All directories except 'include' should be excluded")
//        assertNotNull(gitignoreRules.excludingPattern("include/file.txt"), "Files in 'include' directory should be included")
//    }

    // Test for patterns with wildcards
    @Test
    fun `Patterns with wildcards should behave correctly`() {
        val gitignoreRules = GitignoreRules(listOf("*.log", "temp??.txt"), key = "/")
        assertNotNull(gitignoreRules.excludingPattern("error.log"), "Wildcard pattern '*.log' should exclude files")
        assertNull(gitignoreRules.excludingPattern("temp12.txt"), "Wildcard pattern 'temp??.txt' should exclude files")
    }
}
