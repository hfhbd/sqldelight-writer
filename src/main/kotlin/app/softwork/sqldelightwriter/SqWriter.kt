package app.softwork.sqldelightwriter

@SqDsl
public class SqWriter(
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
                name = name, packageName = packageName, content = queryFile.toString()
            )
        )
    }

    @SqDsl
    public fun migrationFile(
        version: Int,
        packageName: String = this.packageName,
        migration: SqMigrationFile.() -> Unit
    ) {
        check(Migration(version, content = "", packageName) !in this.migrations) {
            "File $version.sqm already defined in package $packageName"
        }

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
}
