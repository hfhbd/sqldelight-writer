package app.softwork.sqldelightwriter

import java.io.*

public class SqFiles(public val migrations: Set<SqMigrationFile>, public val queries: Set<SqQueryFile>) {
    /**
     * This implementation overwrites the migrations/queries with the one from [new].
     * Use [writeSq] if you want to extend them.
     */
    public operator fun plus(new: SqFiles): SqFiles = SqFiles(
        migrations = new.migrations + migrations,
        queries = new.queries + queries
    )

    public fun writeTo(folder: File) {
        for (migration in migrations) {
            val file = File(migration.createPackageFolder(folder), "${migration.version}.sqm")
            file.writeText(migration.toString())
        }
        for (query in queries) {
            val file = File(query.createPackageFolder(folder), "${query.fileName}.sq")
            file.writeText(query.toString())
        }
    }

    private fun SqFile.createPackageFolder(folder: File): File {
        val packageName = packageName.replace(".", "/")
        val packageFolder = File(folder, packageName)
        packageFolder.mkdirs()
        return packageFolder
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SqFiles

        if (migrations != other.migrations) return false
        return queries == other.queries
    }

    override fun hashCode(): Int {
        var result = migrations.hashCode()
        result = 31 * result + queries.hashCode()
        return result
    }
}
