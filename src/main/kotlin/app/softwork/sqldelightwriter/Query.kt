package app.softwork.sqldelightwriter

public class Query(
    public val name: String,
    override val content: String,
    override val packageName: String
) : SqFile {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Query

        if (name != other.name) return false
        if (packageName != other.packageName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + packageName.hashCode()
        return result
    }

    override fun toString(): String = content
}
