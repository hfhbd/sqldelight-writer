package app.softwork.sqldelightwriter

import kotlin.test.Test
import kotlin.test.assertEquals

class ExistingTest {
    @Test
    fun migrationTest() {
        val foo = writeSq("sample") {
            migrationFile(version = 1) {
                +"""
                |CREATE TABLE foo(
                |id INTEGER
                |)
                """.trimMargin()
            }
        }

        val bar = writeSq("sample", existingFiles = foo) {
            migrationFile(version = 1) {
                +"""
                |CREATE TABLE bar(
                |id INTEGER
                |);
                """.trimMargin()
            }
        }

        assertEquals(
            """
            |CREATE TABLE foo(
            |id INTEGER
            |);
            |
            |CREATE TABLE bar(
            |id INTEGER
            |);
            |
            """.trimMargin(),
            bar.migrations.single().toString()
        )
    }

    @Test
    fun queryTest() {
        val foo = writeSq("sample") {
            queryFile(name = "Foo") {
                +"""
                |CREATE TABLE foo(
                |id INTEGER
                |);
                """.trimMargin()
                query("getAll") {
                    +"SELECT * FROM foo;"
                }
                query("getAllMulti") {
                    +"SELECT * FROM foo;"
                    +"SELECT * FROM foo;"
                }
            }
            queryFile(name = "Foo2") {
                query("getAll") {
                    +"SELECT * FROM foo;"
                }
                query("getAllMulti") {
                    +"SELECT * FROM foo;"
                    +"SELECT * FROM foo;"
                }
            }
        }

        val bar = writeSq("sample", existingFiles = foo) {
            queryFile(name = "Foo") {
                +"""
                |CREATE TABLE bar(
                |id INTEGER
                |);
                """.trimMargin()
                query("getAllBar") {
                    +"SELECT * FROM bar;"
                }
            }
            queryFile(name = "Foo2") {
                +"""
                |CREATE TABLE foo2(
                |id INTEGER
                |);
                """.trimMargin()
            }
        }

        val (fooFile, foo2File) = bar.queries.sortedBy { it.fileName }

        assertEquals(
            """
            |CREATE TABLE foo(
            |id INTEGER
            |);
            |
            |CREATE TABLE bar(
            |id INTEGER
            |);
            |
            |getAll:
            |SELECT * FROM foo;
            |
            |getAllMulti {
            |SELECT * FROM foo;
            |SELECT * FROM foo;
            |}
            |
            |getAllBar:
            |SELECT * FROM bar;
            |
            """.trimMargin(),
            fooFile.toString()
        )

        assertEquals(
            """
            |CREATE TABLE foo2(
            |id INTEGER
            |);
            |
            |getAll:
            |SELECT * FROM foo;
            |
            |getAllMulti {
            |SELECT * FROM foo;
            |SELECT * FROM foo;
            |}
            |
            """.trimMargin(),
            foo2File.toString()
        )
    }
}
