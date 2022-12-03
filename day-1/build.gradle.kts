plugins {
  java
  alias(libs.plugins.spotless)
  alias(libs.plugins.gradleVersions)
}

group = "me.jbduncan.adventofcode2022.day1"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
    vendor.set(JvmVendorSpec.AZUL)
  }
}

dependencies {
  implementation(libs.guava)
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
}

spotless {
  java {
    googleJavaFormat("1.15.0")
  }
}
