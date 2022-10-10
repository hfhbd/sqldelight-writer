package app.softwork.sqldelightwriter

public class Migration(
    public val version: Int,
    override val content: String,
    override val packageName: String
) : SqFile {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Migration

        if (version != other.version) return false
        if (packageName != other.packageName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = version
        result = 31 * result + packageName.hashCode()
        return result
    }

    override fun toString(): String = content
}
