# SqlDelight-Writer

Simple DSL to write SqlDelight files

## Install

This package is uploaded to MavenCentral.

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("app.softwork:sqldelight-writer:LATEST")
}
```

## Usage

```kotlin
val files = writeSq(packageName = "sample") {
    migrationFile(version = 1) {
        +"""
        |CREATE TABLE bar(
        |id INTEGER
        |);
        """.trimMargin()
    }
    queryFile(name = "Sql") {
        import("kotlin.Int")

        +"""
        |CREATE TABLE bar(
        |id INTEGER AS Int
        |);
        """.trimMargin()

        query(name = "get") {
            +"""
            |SELECT * FROM bar;
            """.trimMargin()
        }
        query(name = "multi") {
            +"SELECT * FROM bar;"
            +"SELECT * FROM bar;"
        }
    }
}
files.writeTo(folder)
```
