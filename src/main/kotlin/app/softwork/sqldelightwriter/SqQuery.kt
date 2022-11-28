package app.softwork.sqldelightwriter

@SqDsl
public class SqQuery(private val name: String, private val kDoc: List<String>) {
    private val statements = mutableListOf<String>()

    public operator fun String.unaryPlus() {
        statements += if (endsWith(";")) this else "$this;"
    }

    override fun toString(): String = buildString {
        if (kDoc.isNotEmpty()) {
            appendLine("/**")
        }
        for (doc in kDoc) {
            appendLine(" * $doc")
        }
        if (kDoc.isNotEmpty()) {
            appendLine(" */")
        }
        append(name)
        if (statements.size == 1) {
            appendLine(':')
            appendLine(statements.single())
        } else {
            appendLine(" {")
            for (statement in statements) {
                appendLine(statement)
            }
            appendLine('}')
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SqQuery

        if (name != other.name) return false
        if (statements != other.statements) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + statements.hashCode()
        return result
    }
}
