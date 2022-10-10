package app.softwork.sqldelightwriter

import java.io.*

public data class SqFiles(val migrations: Set<Migration>, val queries: Set<Query>) {
    /**
     * This implementation overwrites these migrations/queries with the one from [new]
     */
    public operator fun plus(new: SqFiles): SqFiles = SqFiles(
        migrations = new.migrations + migrations, queries = new.queries + queries
    )

    public fun writeTo(folder: File) {
        for (migration in migrations) {
            val file = File(migration.createPackageFolder(folder), "${migration.version}.sqm")
            file.writeText(migration.content)
        }
        for (query in queries) {
            val file = File(query.createPackageFolder(folder), "${query.name}.sq")
            file.writeText(query.content)
        }
    }

    private fun SqFile.createPackageFolder(folder: File): File {
        val packageName = packageName.replace(".", "/")
        val packageFolder = File(folder, packageName)
        packageFolder.mkdirs()
        return packageFolder
    }
}
