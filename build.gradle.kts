plugins {
    id("java")
}

allprojects {
    group = "fr.darklash.regensystem"
    version = project.findProperty("version") ?: "dev"

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://jitpack.io/")
    }
}
