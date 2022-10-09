import java.io.*

public data class SqFiles(val migrations: Set<Migration>, val queries: Set<Query>) {
    /**
     * This implementation overwrites these migrations/queries with the one from [new]
     */
    public operator fun plus(new: SqFiles): SqFiles = SqFiles(
        migrations = new.migrations + migrations, queries = new.queries + queries
    )

    public fun writeTo(file: File) {
        for (migration in migrations) {
            val packageName = migration.packageName.replace(".", "/")
            val packageFolder = File(file, packageName)
            packageFolder.mkdirs()
            val file = File(packageFolder, "${migration.version}.sqm")
            file.writeText(migration.content)
        }
        for (query in queries) {
            val packageName = query.packageName.replace(".", "/")
            val packageFolder = File(file, packageName)
            packageFolder.mkdirs()
            val file = File(packageFolder, "${query.name}.sq")
            file.writeText(query.content)
        }
    }
}
