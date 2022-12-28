package app.softwork.sqldelightwriter

@SqDsl
public class SqMigrationFile(
    public val version: Int,
    override val packageName: String
) : SqFile {
    private val imports = mutableListOf<String>()
    private val content = mutableListOf<String>()

    public fun import(vararg import: String) {
        imports.addAll(import)
    }
    
    public operator fun String.unaryPlus() {
        content += this
    }

    override fun toString(): String = buildString { 
        for (import in imports) {
            if (isNotEmpty()) {
                appendLine()
            }
            appendSql(import)
        }
        for (content in content) {
            if (isNotEmpty()) {
                appendLine()
            }
            appendSql(content)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SqMigrationFile

        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int = content.hashCode()
}
