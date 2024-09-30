plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.consti"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation(project(":bukkit"))
    implementation(project(":velocity"))
}

tasks {
    // Configure the existing shadowJar task, don't register a new one
    shadowJar {
        // Include the compiled outputs of core, bukkit, and velocity
        from(project(":core").takeIf { it.plugins.hasPlugin("java") }?.sourceSets?.main?.get()?.output ?: files())
        from(project(":bukkit").takeIf { it.plugins.hasPlugin("java") }?.sourceSets?.main?.get()?.output ?: files())
        from(project(":velocity").takeIf { it.plugins.hasPlugin("java") }?.sourceSets?.main?.get()?.output ?: files())

        configurations = listOf(project.configurations.runtimeClasspath.get())
    }


    val copyToBukkitPlugins by creating(Copy::class) {
        dependsOn(shadowJar)
        from(shadowJar.get().outputs.files)
        into("/mnt/FastStorage/Server-TEST/SocketLib/Bukkit/plugins")
    }

    val copyToVelocityPlugins by creating(Copy::class) {
        dependsOn(shadowJar)
        from(shadowJar.get().outputs.files)
        into("/mnt/FastStorage/Server-TEST/SocketLib/Velocity/plugins")
    }

    register("dev") {
        dependsOn(copyToBukkitPlugins, copyToVelocityPlugins)
    }
}
