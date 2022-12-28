package app.softwork.sqldelightwriter

import kotlin.test.*

class ImportTest {
    @Test
    fun imports() {
        val sqFiles = writeSq("sample") {
            queryFile(name = "Sql") {
                import("kotlin.Int")
                +"""
                |CREATE TABLE foo(
                |id INTEGER AS Int
                |);
                """.trimMargin()
            }
            migrationFile(1) {
                import("kotlin.Int")
                +"""
                |CREATE TABLE foo(
                |id INTEGER AS Int
                |);
                """.trimMargin()
            }
        }

        assertEquals(
            """
            |import kotlin.Int;
            |
            |CREATE TABLE foo(
            |id INTEGER AS Int
            |);
            |
            """.trimMargin(),
            sqFiles.queries.single().toString()
        )

        assertEquals(
            """
            |import kotlin.Int;
            |
            |CREATE TABLE foo(
            |id INTEGER AS Int
            |);
            |
            """.trimMargin(),
            sqFiles.migrations.single().toString()
        )
    }
}
