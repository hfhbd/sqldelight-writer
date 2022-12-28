import io.gitlab.arturbosch.detekt.*

plugins {
    kotlin("jvm") version "1.8.0"
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    id("app.cash.licensee") version "1.6.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    explicitApi()
    target {
        compilations.all {
            kotlinOptions.allWarningsAsErrors = true
        }
    }
    sourceSets {
        all {
            languageSettings {
                progressiveMode = true
            }
        }
    }
}

licensee {
    allow("Apache-2.0")
}

publishing {
    publications.register<MavenPublication>("mavenJava") {
        from(components["java"])
    }
    publications.all {
        this as MavenPublication
        pom {
            name.set("app.softwork SqlDelight Writer")
            description.set("A jvm library to create SqlDelight Files")
            url.set("https://github.com/hfhbd/sqldelight-writer")
            licenses {
                license {
                    name.set("Apache-2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("hfhbd")
                    name.set("Philip Wedemann")
                    email.set("mybztg+mavencentral@icloud.com")
                }
            }
            scm {
                connection.set("scm:git://github.com/hfhbd/sqldelight-writer.git")
                developerConnection.set("scm:git://github.com/hfhbd/sqldelight-writer.git")
                url.set("https://github.com/hfhbd/sqldelight-writer")
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

detekt {
    source = files(rootProject.rootDir)
    parallel = true
    buildUponDefaultConfig = true
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
}

tasks {
    fun SourceTask.config() {
        include("**/*.kt")
        exclude("**/*.kts")
        exclude("**/resources/**")
        exclude("**/generated/**")
        exclude("**/build/**")
    }
    withType<DetektCreateBaselineTask>().configureEach {
        config()
    }
    withType<Detekt>().configureEach {
        config()

        reports {
            sarif.required.set(true)
        }
    }
}
