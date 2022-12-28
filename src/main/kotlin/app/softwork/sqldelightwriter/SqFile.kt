package app.softwork.sqldelightwriter

public sealed interface SqFile {
    public val packageName: String
}

internal fun StringBuilder.appendSql(stmt: String) {
    if (stmt.endsWith(";")) {
        appendLine(stmt)
    } else {
        append(stmt)
        appendLine(";")
    }
}
