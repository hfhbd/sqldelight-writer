package app.softwork.sqldelightwriter

@SqDsl
public fun writeSq(packageName: String, existingFiles: SqFiles? = null, body: SqWriter.() -> Unit): SqFiles {
    val writer = SqWriter(packageName, existingFiles)
    writer.body()
    return SqFiles(
        migrations = writer.migrationFiles,
        queries = writer.queryFiles
    )
}
