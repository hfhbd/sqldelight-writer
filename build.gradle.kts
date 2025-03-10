plugins {
    kotlin("jvm") version "2.1.10"
    id("maven-publish")
    id("signing")
    id("io.github.hfhbd.mavencentral") version "0.0.15"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.17.0"
    id("app.cash.licensee") version "1.12.0"
}

testing.suites.named<JvmTestSuite>("test") {
    useKotlinTest()
}

kotlin {
    jvmToolchain(8)

    explicitApi()
    compilerOptions {
        allWarningsAsErrors.set(true)
        progressiveMode.set(true)
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
    val signingKey = providers.gradleProperty("signingKey")
    if (signingKey.isPresent) {
        useInMemoryPgpKeys(signingKey.get(), providers.gradleProperty("signingPassword").get())
        sign(publishing.publications)
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

detekt {
    source.from(fileTree(rootProject.rootDir) {
        include("**/*.kt")
        exclude("**/*.kts")
        exclude("**/resources/**")
        exclude("**/generated/**")
        exclude("**/build/**")
    })
    parallel = true
    autoCorrect = true
    buildUponDefaultConfig = true
    reports {
        sarif.required.set(true)
    }
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${detekt.toolVersion}")
}
