import io.gitlab.arturbosch.detekt.*
import java.util.*

plugins {
    kotlin("jvm") version "1.9.10"
    id("maven-publish")
    id("signing")
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.13.2"
    id("app.cash.licensee") version "1.7.0"
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    explicitApi()
    target {
        compilations.configureEach {
            kotlinOptions.allWarningsAsErrors = true
        }
    }
    jvmToolchain(8)
    
    sourceSets {
        configureEach {
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
    publications.register<MavenPublication>("maven") {
        from(components["java"])
    }
    publications.configureEach {
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
    useInMemoryPgpKeys(signingKey?.let { String(Base64.getDecoder().decode(it)).trim() }, signingPassword)
    sign(publishing.publications)
}

nexusPublishing {
    this.repositories {
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
    source.from(files(rootProject.rootDir))
    parallel = true
    buildUponDefaultConfig = true
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${detekt.toolVersion}")
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
        autoCorrect = true
        reports {
            sarif.required.set(true)
        }
    }
}
