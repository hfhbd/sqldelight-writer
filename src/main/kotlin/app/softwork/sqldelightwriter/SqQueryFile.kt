package app.softwork.sqldelightwriter

@SqDsl
public class SqQueryFile(private val fileName: String) {
    private val topLevel = mutableListOf<String>()
    private val queries = mutableMapOf<String, SqQuery>()

    @SqDsl
    public fun query(name: String, query: SqQuery.() -> Unit) {
        check(name !in queries) {
            "Query identifier $name already defined in $fileName.sq"
        }
        val statements = SqQuery(name)
        statements.query()
        queries[name] = statements
    }

    public operator fun String.unaryPlus() {
        topLevel += this
    }

    override fun toString(): String = buildString {
        for (topLevel in topLevel) {
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
