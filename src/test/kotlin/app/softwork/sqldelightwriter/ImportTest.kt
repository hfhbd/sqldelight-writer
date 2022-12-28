package app.softwork.sqldelightwriter

import java.io.*
import java.nio.file.*
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
    }
}
