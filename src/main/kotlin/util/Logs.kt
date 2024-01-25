package util

object Logs {
    fun log(
        message: String,
        isError: Boolean = false,
        isFine: Boolean = false,
    ) {
        val emoji =
            when {
                isError -> "❌"
                isFine -> "🔍"
                else -> "ℹ️"
            }
        println("$emoji $message")
    }

    fun log(
        message: String,
        isError: Boolean = false,
        isInfo: Boolean = false,
        prefix: String? = null,
    ) {
        val emoji =
            when {
                isError -> "❌"
                isInfo -> "ℹ️"
                prefix != null -> prefix
                else -> "🔍"
            }
        println("$emoji $message")
    }
}
