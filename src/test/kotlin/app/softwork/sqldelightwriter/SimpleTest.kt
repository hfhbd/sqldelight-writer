package app.softwork.sqldelightwriter

import java.io.File
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class SimpleTest {
    @Test
    fun queries() {
        val sqFiles = writeSq("sample") {
            queryFile(name = "Sql") {
                +"""
                |CREATE TABLE foo(
                |id INTEGER
                |);
                """.trimMargin()

                +"""
                |CREATE TABLE bar(
                |id INTEGER
                |);
                """.trimMargin()

                query(name = "get", "TEST DOC", "TEST DOC II") {
                    +"""
                    |SELECT * FROM bar
                    """.trimMargin()
                }
                query("multi") {
                    +"SELECT * FROM bar;"
                    +"SELECT * FROM bar;"
                }
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
            |/**
            | * TEST DOC
            | * TEST DOC II
            | */
            |get:
            |SELECT * FROM bar;
            |
            |multi {
            |SELECT * FROM bar;
            |SELECT * FROM bar;
            |}
            |
            """.trimMargin(),
            sqFiles.queries.single().toString()
        )
    }

    @Test
    fun testMigration() {
        val sqFiles = writeSq("sample") {
            migrationFile(version = 1) {
                +"""
                |CREATE TABLE foo(
                |id INTEGER
                |);
                """.trimMargin()

                +"""
                |CREATE TABLE bar(
                |id INTEGER
                |);
                """.trimMargin()
            }
            queryFile(name = "Sql") {
                query(name = "get") {
                    +"""
                    |SELECT * FROM bar;
                    """.trimMargin()
                }
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
            sqFiles.migrations.single().toString()
        )

        assertEquals(
            """
            |get:
            |SELECT * FROM bar;
            |
            """.trimMargin(),
            sqFiles.queries.single().toString()
        )
    }

    @Test
    fun testNameClash() {
        val exception = assertFailsWith<IllegalArgumentException> {
            writeSq("sample") {
                queryFile(name = "Sql") {
                    query(name = "get") {
                        +"SELECT * FROM bar;"
                    }
                    query(name = "get") {
                        +"SELECT * FROM bar;"
                    }
                }
            }
        }

        assertEquals("Query identifier get already defined in Sql.sq in package sample", exception.message)
    }

    @Test
    fun files() {
        val temp = Files.createTempDirectory("sql").toFile()
        val files = writeSq("sample") {
            migrationFile(1) {
                +"""
                |CREATE TABLE bar(
                |id INTEGER
                |);
                """.trimMargin()
            }
            queryFile(name = "Sql") {
                +"""
                |CREATE TABLE bar(
                |id INTEGER
                |);
                """.trimMargin()

                query(name = "get") {
                    +"""
                    |SELECT * FROM bar;
                    """.trimMargin()
                }
                query("multi") {
                    +"SELECT * FROM bar;"
                    +"SELECT * FROM bar;"
                }
            }
        }
        files.writeTo(temp)
        val created = temp.listFiles()?.toList()
        assertNotNull(created)
        assertEquals(1, created.size)
        val sample: File = created.single()
        assertEquals("sample", sample.name)
        assertEquals(listOf("1.sqm", "Sql.sq"), sample.listFiles()?.map { it.name }?.sorted())
    }
}
