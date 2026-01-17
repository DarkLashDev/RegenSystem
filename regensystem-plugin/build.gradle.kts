import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
    alias(libs.plugins.shadow)
    alias(libs.plugins.runPaper)
    alias(libs.plugins.modrinth)
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/releases/")
    maven("https://repo.bstats.org")
}

dependencies {
    implementation(project(":regensystem-api"))

    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")

    compileOnly(libs.placeholderapi)
    compileOnly(libs.lombok)

    implementation(libs.gson)
    implementation(libs.snakeyaml)
    implementation(libs.bstats)
    implementation(libs.adventureapi)
    implementation(libs.adventurebukkit)
    implementation(libs.adventureminimessage)
    implementation(libs.adventureserializergson)
    implementation(libs.adventureserializerlegacy)
    implementation(libs.adventureserializerplain)

    annotationProcessor(libs.lombok)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withSourcesJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(21)
}

tasks {
    runServer {
        minecraftVersion("1.21.10")
    }

    withType<ShadowJar> {
        archiveFileName.set("RegenSystem-${rootProject.version}.jar")

        relocate("org.projectlombok", "fr.darklash.regensystem.libs.lombok")
        relocate("com.google.code.gson", "fr.darklash.regensystem.libs.gson")
        relocate("org.yaml", "fr.darklash.regensystem.libs.snakeyaml")
        relocate("org.bstats", "fr.darklash.regensystem.libs.bstats")
        relocate("me.clip", "fr.darklash.regensystem.libs.placeholderapi")
    }

    build {
        dependsOn(shadowJar)
    }

    jar {
        enabled = false
    }

    processResources {
        val props = mapOf("version" to rootProject.version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN") ?: project.findProperty("modrinthToken")?.toString())
    projectId.set("regensystem")
    versionNumber.set(rootProject.version.toString())
    versionName.set("RegenSystem ${rootProject.version}")
    versionType.set("release")
    uploadFile.set(tasks.named<ShadowJar>("shadowJar").flatMap { it.archiveFile })
    gameVersions.set(
        listOf(
            "1.20", "1.20.1", "1.20.2", "1.20.3", "1.20.4", "1.20.5", "1.20.6",
            "1.21", "1.21.1", "1.21.2", "1.21.3", "1.21.4", "1.21.5", "1.21.6", "1.21.7", "1.21.8", "1.21.10", "1.21.11"
        )
    )
    loaders.set(listOf("bukkit", "paper", "folia", "spigot"))
}
