plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19-R0.1-SNAPSHOT")
    implementation("commons-lang:commons-lang:2.6")
}

tasks {
    compileJava {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
        options.encoding = "UTF-8"
    }

    jar {
        dependsOn("shadowJar")
    }

    shadowJar {
        if (project.hasProperty("cd"))
            archiveFileName.set("RandomCommands.jar")
        else
            archiveFileName.set("RandomCommands-${archiveVersion.getOrElse("unknown")}.jar")

        destinationDirectory.set(file(System.getenv("outputDir") ?: "$rootDir/build/"))

        minimize()
    }
}

bukkit {
    name = "RandomCommands"
    main = "fr.aerwyn81.randomcommands.RandomCommands"
    authors = listOf("AerWyn81")
    apiVersion = "1.13"
    description = "Trigger commands randomly under certain conditions"
    version = project.version.toString()

    commands {
        register("randomcommands") {
            description = "Plugin command"
            aliases = listOf("rc")
        }
    }
}