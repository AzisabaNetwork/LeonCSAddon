plugins {
    id("java-library")
    id("maven-publish")
}

group = "net.azisaba"
version = "1.2D+1.21.11"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:deprecation")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.azisaba.net/repository/proprietary")
    mavenLocal()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("net.azisaba:CrackShot:1.0.2")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "azisaba"
            val releasesRepoUrl = uri("https://maven.azisaba.net/repository/maven-releases/")
            val snapshotsRepoUrl = uri("https://maven.azisaba.net/repository/maven-snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

            credentials {
                username = System.getenv("NEWREPO_USERNAME") ?: (findProperty("azisabaUsername") as? String)
                password = System.getenv("NEWREPO_PASSWORD") ?: (findProperty("azisabaPassword") as? String)
            }
        }
    }
}