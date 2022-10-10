package app.softwork.sqldelightwriter

@SqDsl
public class SqWriter(
    private val packageName: String,
    existing: SqFiles? = null
) {
    internal val migrationFiles = existing?.migrations?.toMutableSet() ?: mutableSetOf()
    internal val queryFiles = existing?.queries?.toMutableSet() ?: mutableSetOf()

    @SqDsl
    public fun queryFile(name: String, packageName: String = this.packageName, queries: SqQueryFile.() -> Unit) {

        val exists = this.queryFiles.find {
            it.fileName == name && it.packageName == packageName
        }
        if (exists != null) {
            exists.queries()
        } else {
            val queryFile = SqQueryFile(name, packageName)
            queryFile.queries()
            queryFiles.add(queryFile)
        }
    }

    @SqDsl
    public fun migrationFile(
        version: Int,
        packageName: String = this.packageName,
        migration: SqMigrationFile.() -> Unit
    ) {
        val exists = migrationFiles.find {
            it.version == version && it.packageName == packageName
        }

        if (exists != null) {
            exists.migration()
        } else {
            val file = SqMigrationFile(version, packageName)
            file.migration()
            migrationFiles.add(file)
        }
    }
}
