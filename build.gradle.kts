plugins {
    kotlin("jvm") version "2.0.21"
}

group = "me.techchrism"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(21)
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand(project.properties)
    }
}

tasks.jar {
    exclude("META-INF/**", "META-INF")
    archiveFileName.set("${project.name}.jar")
}

tasks.register("copyJarToPlugins") {
    doLast {
        copy {
            from("build/libs/${project.name}.jar")
            into(System.getenv("TESTING_PLUGINS_DIR"))
        }
    }
}

tasks.register("buildAndCopy") {
    dependsOn("jar")
    dependsOn("copyJarToPlugins")
    tasks.findByName("copyJarToPlugins")?.mustRunAfter("jar")
}