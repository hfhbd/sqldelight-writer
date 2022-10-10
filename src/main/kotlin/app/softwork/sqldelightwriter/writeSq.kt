package app.softwork.sqldelightwriter

@SqDsl
public fun writeSq(packageName: String, body: SqWriter.() -> Unit): SqFiles {
    val writer = SqWriter(packageName)
    writer.body()
    return SqFiles(
        migrations = writer.migrations,
        queries = writer.queries
    )
}
