plugins {
    java
    alias(libs.plugins.spotless)
    alias(libs.plugins.gradleVersions)
}

group = "me.jbduncan.adventofcode2022.day7"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(libs.versions.java.map(JavaLanguageVersion::of))
        vendor.set(JvmVendorSpec.AZUL)
    }
}

dependencies {
    implementation(libs.guava)
    implementation(libs.jool)
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter(libs.versions.junitJupiter)
            dependencies {
                implementation(libs.truth)
            }
        }
    }
}

tasks.jar {
    manifest {
        attributes["Implementation-Title"] = project.name
        attributes["Implementation-Version"] = archiveVersion
        attributes["Main-Class"] = "${project.group}.App"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

tasks.withType<AbstractArchiveTask> {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
    dirMode = Integer.parseInt("0755", 8)
    fileMode = Integer.parseInt("0644", 8)
}

spotless {
    java {
        googleJavaFormat(libs.versions.googleJavaFormat.get())
    }
}
