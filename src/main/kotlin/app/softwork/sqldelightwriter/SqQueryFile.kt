package app.softwork.sqldelightwriter

@SqDsl
public class SqQueryFile(
    public val fileName: String,
    override val packageName: String
) : SqFile {
    private val topLevel = mutableListOf<String>()
    private val queries = mutableMapOf<String, SqQuery>()

    public val queryNames: Set<String> get() = queries.keys

    @SqDsl
    public fun query(name: String, vararg kdoc: String, query: SqQuery.() -> Unit) {
        query(name, kdoc.toList(), query)
    }

    @SqDsl
    public fun query(name: String, kdoc: List<String>, query: SqQuery.() -> Unit) {
        check(name !in queries) {
            "Query identifier $name already defined in $fileName.sq in package $packageName"
        }
        val statements = SqQuery(name, kdoc)
        statements.query()
        queries[name] = statements
    }

    public operator fun String.unaryPlus() {
        topLevel += this
    }

    override fun toString(): String = buildString {
        for (topLevel in topLevel) {
            if (isNotEmpty()) {
                appendLine()
            }
            appendLine(topLevel)
        }

        for ((_, query) in queries) {
            if (isNotEmpty()) {
                appendLine()
            }
            append(query)
        }
    }
}
