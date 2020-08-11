//variables
val ktorVersion = Versions.ktorVersion
val logbackVersion = Versions.logback
val serializationVersion = Versions.mainLibVersion
val exposedVersion = Versions.kotlinExposedVersion
val h2Version = Versions.h2Version
val coroutineVersion = Versions.coroutineVersion
val react = Versions.react

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.0")
    }
}

plugins {
    kotlin("multiplatform") version Versions.Plugins.kotlin
    kotlin("plugin.serialization") version Versions.Plugins.kotlin
}

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/ktor")
    maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
    maven("https://plugins.gradle.org/m2/")
    google()
    jcenter()
}

kotlin {
    jvm()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutineVersion")

                implementation("io.ktor:ktor-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
            }
        }
        val commonTest by getting {
            dependencies {
//                implementation(kotlin("test-common"))
//                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-html-builder:$ktorVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")
                implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
                implementation("io.ktor:ktor-auth:$ktorVersion")
                implementation("io.ktor:ktor-locations:$ktorVersion")

                //db things
                implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
                implementation("com.h2database:h2:$h2Version")

                //client logging
                implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter:5.6.2")
                implementation("io.ktor:ktor-server-test-host:$ktorVersion")
            }
        }

    }
}

/**
 * https://github.com/kotest/kotest/issues/1105
 */
tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
}

val run by tasks.creating(JavaExec::class) {
    group = "application"
    main = "de.innosystec.kuestion.JvmMainKt"
    kotlin {
        val main = targets["jvm"].compilations["main"]
        dependsOn(main.compileAllTaskName)
        classpath(
            { main.output.allOutputs.files },
            { configurations["jvmRuntimeClasspath"] }
        )
    }
}