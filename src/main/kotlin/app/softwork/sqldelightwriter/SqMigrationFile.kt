package app.softwork.sqldelightwriter

@SqDsl
public class SqMigrationFile(
    public val version: Int,
    override val packageName: String
) : SqFile {
    private val content = mutableListOf<String>()

    public operator fun String.unaryPlus() {
        content += if (endsWith(";")) this else "$this;"
    }

    override fun toString(): String = content.joinToString(
        separator = "${System.lineSeparator()}${System.lineSeparator()}",
        postfix = System.lineSeparator()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SqMigrationFile

        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int = content.hashCode()
}
