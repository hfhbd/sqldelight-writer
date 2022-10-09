@SqDsl
public class SqWriter internal constructor(
    private val packageName: String
) {
    internal val migrations = mutableSetOf<Migration>()
    internal val queries = mutableSetOf<Query>()

    @SqDsl
    public fun queryFile(name: String, packageName: String = this.packageName, queries: SqQueryFile.() -> Unit) {
        check(Query(name, content = "", packageName) !in this.queries) {
            "File $name.sq already defined in package $packageName"
        }
        val queryFile = SqQueryFile(name)
        queryFile.queries()

        this.queries.add(
            Query(
                name = name,
                packageName = packageName,
                content = queryFile.toString()
            )
        )
    }

    @SqDsl
    public class SqQueryFile internal constructor(private val fileName: String) {
        private val topLevel = mutableListOf<String>()
        private val queries = mutableMapOf<String, List<String>>()

        @SqDsl
        public fun query(name: String, query: SqQuery.() -> Unit) {
            check(name !in queries) {
                "Query identifier $name already defined in $fileName.sq"
            }
            val statements = SqQuery()
            statements.query()
            queries[name] = statements.statements()
        }

        public operator fun String.unaryPlus() {
            topLevel += this
        }

        override fun toString(): String = buildString {
            for (topLevel in topLevel) {
                appendLine(topLevel)
            }

            for ((id, content) in queries) {
                if (length != 0) {
                    appendLine()
                }
                append(id)
                if (content.size == 1) {
                    appendLine(':')
                    appendLine(content.single())
                } else {
                    appendLine(" {")
                    for (content in content) {
                        appendLine(content)
                    }
                    appendLine('}')
                }
            }
        }
    }

    @SqDsl
    public fun migrationFile(version: Int, migration: SqMigrationFile.() -> Unit) {
        val file = SqMigrationFile()
        file.migration()
        migrations.add(
            Migration(
                version = version,
                content = file.toString(),
                packageName = packageName
            )
        )
    }

    @SqDsl
    public class SqMigrationFile internal constructor() {
        private val content = mutableListOf<String>()
        public operator fun String.unaryPlus() {
            content += this
        }

        override fun toString(): String = content.joinToString(separator = "\n\n", postfix = "\n")
    }

    @SqDsl
    public class SqQuery internal constructor() {
        private val statements = mutableListOf<String>()
        public operator fun String.unaryPlus() {
            statements += this
        }

        internal fun statements() = statements
    }
}

@SqDsl
public fun writeSq(packageName: String, body: SqWriter.() -> Unit): SqFiles {
    val writer = SqWriter(packageName)
    writer.body()
    return SqFiles(
        migrations = writer.migrations, queries = writer.queries
    )
}
